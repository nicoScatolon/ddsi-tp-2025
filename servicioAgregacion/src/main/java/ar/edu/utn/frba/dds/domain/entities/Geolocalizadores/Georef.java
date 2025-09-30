package ar.edu.utn.frba.dds.domain.entities.Geolocalizadores;

import ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador.GeorefCoordenadasInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador.GeorefDireccionInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.services.impl.HechosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Georef implements IGeoLocalizador {

    private static final Logger logger = LoggerFactory.getLogger(Georef.class);
    private final WebClient webClient;

    private static final String BASE_URL = "https://apis.datos.gob.ar/georef/api";
    private static final int BATCH_SIZE = 50;         // tamaño de lote recomendado
    private static final int MAX_MEMORY_BYTES = 4 * 1024 * 1024; // 4MB para evitar DataBufferLimit


    public Georef() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(c -> c.defaultCodecs().maxInMemorySize(MAX_MEMORY_BYTES))
                                .build()
                )
                .build();
    }

    //----------------------------- METODOS SINCRONICOS -----------------------------

    public Ubicacion geolocalizar(Ubicacion ubi) {
        if ( (ubi.getLatitud() == null || ubi.getLongitud() == null)
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null) {
            // caso A -> no tenemos latitud y longitud pero tenemos provincia, departamento, calle y numer
            return this.obtenerCoordenadas(ubi);
        } else if ((ubi.getDepartamento() == null || ubi.getProvincia() == null)
                && ubi.getLatitud() != null
                && ubi.getLongitud() != null) {
            // caso B -> no tenemos provincia y departamento pero tenemos latitud y longitud
            return this.obtenerDireccion(ubi);
        } else if (ubi.getLatitud() != null
                && ubi.getLongitud() != null
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null){
            // caso C -> la ubicacion esta completa, no necesito nada
            return ubi;
        }
        else {
            throw new RuntimeException("La ubicación no es apta para geolocalizar");
        }
    }

    public Ubicacion obtenerDireccion(Ubicacion ubicacion) {
        if (ubicacion.getLatitud() == null || ubicacion.getLongitud() == null) {
            throw new IllegalArgumentException("Latitud o longitud nulas en Ubicacion: " + ubicacion);
        }
        GeorefDireccionInputDTO georefDireccionDTO =  this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ubicacion")
                        .queryParam("lat", ubicacion.getLatitud())
                        .queryParam("lon", ubicacion.getLongitud())
                        .build())
                .retrieve()
                .bodyToMono(GeorefDireccionInputDTO.class)
                .block();

        if (georefDireccionDTO == null) {
            throw new RuntimeException("No se pudo obtener respuesta del servicio Georef");
        }

        if (georefDireccionDTO.getProvincia() == null) {
            ubicacion.setProvincia("DESCONOCIDA");
            ubicacion.setDepartamento(null);
            ubicacion.setCalle(null);
            ubicacion.setNumero(null);
        } else {
            ubicacion.setProvincia(georefDireccionDTO.getProvincia());
        }

        ubicacion.setDepartamento( georefDireccionDTO.getDepartamento() );
        return ubicacion;
    }

    public Ubicacion obtenerCoordenadas(Ubicacion ubicacion){
        String direccion = buildDireccion(ubicacion.getCalle(), ubicacion.getNumero());

        GeorefCoordenadasInputDTO georefCoordenadasDTO =  this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/direcciones")
                        .queryParam("direccion", direccion)
                        .queryParam("provincia", ubicacion.getProvincia())
                        .queryParam("departamento", ubicacion.getDepartamento())
                        .queryParam("max", 1) // opcional: limitar resultados
                        .build())
                .retrieve()
                .bodyToMono(GeorefCoordenadasInputDTO.class)
                .block();
        if (georefCoordenadasDTO.getLatitud(0) == null || georefCoordenadasDTO.getLongitud(0) == null){
            throw new RuntimeException("Latitud y longitud no validos");
        }

        ubicacion.setLatitud( georefCoordenadasDTO.getLatitud(0) );
        ubicacion.setLongitud( georefCoordenadasDTO.getLongitud(0) );
        return ubicacion;
    }


    private String buildDireccion(String calle, Integer numero) {
        if (calle == null || calle.isBlank()) {
            throw new IllegalArgumentException("La calle es obligatoria para georreferenciar");
        }
        if (numero != null) {
            return calle.trim() + " " + numero;
        }
        return calle.trim();
    }


    // ---------------------------------- METODOS ASINCRÓNICOS ----------------------------------

    public Mono<List<Ubicacion>> geolocalizarBatchAsync(List<Ubicacion> ubicaciones) {
        if (ubicaciones == null || ubicaciones.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }

        // ---------- AJUSTES MINIMOS PARA LLAMAR CONCURRENTEMENTE Y QUE GEOREF NO EXPLOTE ----------
        final int CONCURRENCIA = 4;
        final Duration PAUSA_ENTRE_CHUNKS = Duration.ofMillis(150);
        final Retry RETRY_SUAVE = Retry.backoff(2, Duration.ofSeconds(1))
                .maxBackoff(Duration.ofSeconds(5))
                .jitter(0.3)
                .filter(ex -> ex instanceof WebClientResponseException w &&
                        (w.getStatusCode().value() == 429 || w.getStatusCode().is5xxServerError()));
        // -------------------------------------

        // Separar por casos
        List<Ubicacion> casoA = new ArrayList<>(); // direccion -> coords
        List<Ubicacion> casoB = new ArrayList<>(); // coords -> jurisdicciones
        List<Ubicacion> casoC = new ArrayList<>();

        for (Ubicacion u : ubicaciones) {
            if (u == null) continue;

            boolean tieneCoords  = u.getLatitud() != null && u.getLongitud() != null;
            boolean tieneJurisd  = u.getProvincia() != null && u.getDepartamento() != null;
            boolean tieneDirecc = u.getCalle() != null && u.getNumero() != null;

            if (!tieneCoords && tieneJurisd && tieneDirecc) {
                casoA.add(u);
            } else if (tieneCoords && !tieneJurisd) {
                casoB.add(u);
            } else {
                casoC.add(u);
            }
        }

        // Armar flujos con FLUX

        Flux<Ubicacion> completasFlux = Flux.fromIterable(casoC);

        Flux<Ubicacion> forwardFlux = chunk(casoA, BATCH_SIZE)
                .delayElements(PAUSA_ENTRE_CHUNKS)
                .flatMap(list ->
                                callDireccionesBulk(list)
                                        .retryWhen(RETRY_SUAVE),
                                CONCURRENCIA)
                .onErrorContinue((ex, obj) -> {
                    logger.error("Error en /direcciones bulk: {}", ex.getMessage());
                });

        Flux<Ubicacion> reverseFlux = chunk(casoB, BATCH_SIZE)
                .delayElements(PAUSA_ENTRE_CHUNKS)
                .flatMap(list ->
                                callUbicacionBulk(list)
                                        .retryWhen(RETRY_SUAVE),
                        CONCURRENCIA)
                .onErrorContinue((ex, obj) -> {
                    logger.error("Error en /ubicacion bulk: {}", ex.getMessage());
                });

        return Flux.merge(completasFlux, forwardFlux, reverseFlux)
                // Volvemos en el mismo orden de entrada
                .collectList()
                .map(updated -> {
                    Map<Ubicacion, Ubicacion> indexByIdentity = updated.stream()
                            .collect(Collectors.toMap(Function.identity(), Function.identity(), (a, b) -> a, LinkedHashMap::new));
                    // No cambiamos referencias originales: se actualiza in-place en los bulks
                    return ubicaciones;
                });
    }

    // -----------------------------
    // Llamadas BULK reales (POST) --> Manipulación asincrónica para hacer el Post de muchas ubicaciones en simultaneo
    // -----------------------------

    /**
     * POST /direcciones – normalización de direcciones => set lat/lon (+ jurisdicción si viene)
     */
    private Flux<Ubicacion> callDireccionesBulk(List<Ubicacion> lote) {
        if (lote == null || lote.isEmpty()) return Flux.empty();

        DireccionesRequest body = new DireccionesRequest();
        body.direcciones = lote.stream()
                .map(u -> {
                    DireccionesRequest.Item it = new DireccionesRequest.Item();
                    it.direccion = buildDireccion(u.getCalle(), u.getNumero());
                    it.max = 1;
                    it.campos = "basico"; // alcanza para lat/lon + jurisdicciones
                    if (u.getProvincia() != null)   it.provincia = u.getProvincia();
                    if (u.getDepartamento() != null) it.departamento = u.getDepartamento();
                    return it;
                })
                .collect(Collectors.toList());

        return webClient.post()
                .uri("/direcciones")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(DireccionesResponse.class)
                .flatMapMany(resp -> mapDireccionesResponse(resp, lote))
                .onErrorResume(ex -> {
                    logger.error("Fallo /direcciones: {}", ex.getMessage());
                    return Flux.fromIterable(lote); // devolvemos sin cambios
                });
    }

    private Flux<Ubicacion> mapDireccionesResponse(DireccionesResponse resp, List<Ubicacion> originales) {
        if (resp == null || resp.resultados == null) return Flux.fromIterable(originales);

        // El API devuelve resultados preservando el orden de entrada
        List<Ubicacion> updated = new ArrayList<>();
        int idx = 0;

        for (DireccionesResponse.Result r : resp.resultados) {
            if (r.direcciones == null) continue;
            for (DireccionesResponse.Direccion d : r.direcciones) {
                if (idx >= originales.size()) break;
                Ubicacion u = originales.get(idx++);

                if (d.ubicacion != null) {
                    if (d.ubicacion.lat != null) u.setLatitud(d.ubicacion.lat);
                    if (d.ubicacion.lon != null) u.setLongitud(d.ubicacion.lon);
                }
                if (d.provincia != null && d.provincia.nombre != null) {
                    u.setProvincia(d.provincia.nombre);
                }
                if (d.departamento != null && d.departamento.nombre != null) {
                    u.setDepartamento(d.departamento.nombre);
                }
                updated.add(u);
            }
        }
        return Flux.fromIterable(updated.isEmpty() ? originales : updated);
    }

    /**
     * POST /ubicacion – georreferenciación inversa => set provincia/departamento
     */

    private Flux<Ubicacion> callUbicacionBulk(List<Ubicacion> lote) {
        if (lote == null || lote.isEmpty()) return Flux.empty();

        UbicacionRequest body = new UbicacionRequest();
        body.ubicaciones = lote.stream()
                .map(u -> {
                    UbicacionRequest.Item it = new UbicacionRequest.Item();
                    it.lat = u.getLatitud();
                    it.lon = u.getLongitud();
                    it.campos = "completo";
                    return it;
                })
                .collect(Collectors.toList());
        body.aplanar = false; // mantenemos estructura estándar

        return webClient.post()
                .uri("/ubicacion")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(UbicacionResponse.class)
                .flatMapMany(resp -> mapUbicacionResponse(resp, lote))
                .onErrorResume(ex -> {
                    logger.error("Fallo /ubicacion: {}", ex.getMessage());
                    // marcar desconocidos para que no bloquee el flujo
                    lote.forEach(u -> {
                        if (u.getProvincia() == null) u.setProvincia("DESCONOCIDA");
                        if (u.getDepartamento() == null) u.setDepartamento(null);
                    });
                    return Flux.fromIterable(lote);
                });
    }

    private Flux<Ubicacion> mapUbicacionResponse(UbicacionResponse resp, List<Ubicacion> originales) {
        if (resp == null || resp.resultados == null) return Flux.fromIterable(originales);

        List<Ubicacion> updated = new ArrayList<>();
        int idx = 0;

        for (UbicacionResponse.Result r : resp.resultados) {
            if (r.ubicaciones == null) continue;
            for (UbicacionResponse.Item it : r.ubicaciones) {
                if (idx >= originales.size()) break;
                Ubicacion u = originales.get(idx++);
                if (it.ubicacion != null) {
                    if (it.ubicacion.provincia != null && it.ubicacion.provincia.nombre != null) {
                        u.setProvincia(it.ubicacion.provincia.nombre);
                    } else if (u.getProvincia() == null) {
                        u.setProvincia("DESCONOCIDA");
                    }
                    if (it.ubicacion.departamento != null && it.ubicacion.departamento.nombre != null) {
                        u.setDepartamento(it.ubicacion.departamento.nombre);
                    }
                    // municipio está disponible si lo necesitás
                } else {
                    if (u.getProvincia() == null) u.setProvincia("DESCONOCIDA");
                    u.setDepartamento(null);
                }
                updated.add(u);
            }
        }
        return Flux.fromIterable(updated.isEmpty() ? originales : updated);
    }

    // -----------------------------
    // Helpers y DTOs internos
    // -----------------------------

    private static <T> Flux<List<T>> chunk(List<T> list, int size) {
        if (list == null || list.isEmpty()) return Flux.empty();
        int total = list.size();
        List<List<T>> partes = new ArrayList<>((total + size - 1) / size);
        for (int i = 0; i < total; i += size) {
            partes.add(list.subList(i, Math.min(total, i + size)));
        }
        return Flux.fromIterable(partes);
    }


    // Para no cambiar referencias del caller en el método sync
    private static Ubicacion cloneUbicacion(Ubicacion u) {
        Ubicacion c = new Ubicacion();
        c.setProvincia(u.getProvincia());
        c.setDepartamento(u.getDepartamento());
        c.setCalle(u.getCalle());
        c.setNumero(u.getNumero());
        c.setLatitud(u.getLatitud());
        c.setLongitud(u.getLongitud());
        return c;
    }

    private static void copyUbicacion(Ubicacion src, Ubicacion dst) {
        if (src.getProvincia() != null)   dst.setProvincia(src.getProvincia());
        if (src.getDepartamento() != null) dst.setDepartamento(src.getDepartamento());
        if (src.getLatitud() != null)     dst.setLatitud(src.getLatitud());
        if (src.getLongitud() != null)    dst.setLongitud(src.getLongitud());
        if (src.getCalle() != null)       dst.setCalle(src.getCalle());
        if (src.getNumero() != null)      dst.setNumero(src.getNumero());
    }

    // ====== DTOs /direcciones ======
    static class DireccionesRequest {
        public List<Item> direcciones;

        static class Item {
            public String direccion;
            public Integer max;         // normalmente 1
            public String campos;       // "basico" o "completo"
            public String provincia;    // opcional
            public String departamento; // opcional
        }
    }

    static class DireccionesResponse {
        public List<Result> resultados;

        static class Result {
            public Integer cantidad;
            public Integer total;
            public Integer inicio;
            public Map<String, Object> parametros;
            public List<Direccion> direcciones;
        }

        static class Direccion {
            public Altura altura;
            public Calle  calle;
            public String nomenclatura;
            public Punto  ubicacion;
            public Jurisdiccion provincia;
            public Jurisdiccion departamento;
            // hay más campos (localidad_censal, etc.) si los necesitás
        }

        static class Altura { public Integer valor; public String unidad; }
        static class Calle { public String id; public String nombre; public String categoria; }
        static class Punto { public Double lat; public Double lon; }
    }

    // ====== DTOs /ubicacion ======
    static class UbicacionRequest {
        public List<Item> ubicaciones;
        public Boolean aplanar; // true/false

        static class Item {
            public Double lat;
            public Double lon;
            public String campos; // "basico" o "completo"
        }
    }

    static class UbicacionResponse {
        public List<Result> resultados;

        static class Result {
            public Integer cantidad;
            public Map<String, Object> parametros;
            public List<Item> ubicaciones;
        }

        static class Item {
            public UbicacionDetallada ubicacion;
        }

        static class UbicacionDetallada {
            public Double lat;
            public Double lon;
            public Jurisdiccion provincia;
            public Jurisdiccion departamento;
            public Jurisdiccion municipio;
        }
    }

    // ====== DTO compartido ======
    static class Jurisdiccion {
        public String id;
        public String nombre;
    }
}


