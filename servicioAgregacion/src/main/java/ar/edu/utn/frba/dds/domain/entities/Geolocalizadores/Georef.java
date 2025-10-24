package ar.edu.utn.frba.dds.domain.entities.Geolocalizadores;

import ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador.GeorefCoordenadasInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.geolocalizador.GeorefDireccionInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Georef implements IGeoLocalizador {

    private static final Logger logger = LoggerFactory.getLogger(Georef.class);
    private final WebClient webClient;

    private static final String BASE_URL = "https://apis.datos.gob.ar/georef/api";
    private static final int BATCH_SIZE = 50;
    private static final int MAX_MEMORY_BYTES = 4 * 1024 * 1024; // 4MB

    // Cache simple para normalizaciones (las provincias no cambian)
    private final Map<String, String> provinciasCache = new ConcurrentHashMap<>();

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
        if ((ubi.getLatitud() == null || ubi.getLongitud() == null)
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null) {
            // caso A -> no tenemos latitud y longitud pero tenemos provincia, departamento, calle y numero
            // Normalizar provincia antes de buscar coordenadas
            String provinciaNormalizada = normalizarProvinciaSincrono(ubi.getProvincia());
            ubi.setProvincia(provinciaNormalizada);
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
                && ubi.getNumero() != null) {
            // caso C -> la ubicacion esta completa, no necesito nada
            return ubi;
        } else {
            throw new RuntimeException("La ubicación no es apta para geolocalizar");
        }
    }

    public Ubicacion obtenerDireccion(Ubicacion ubicacion) {
        if (ubicacion.getLatitud() == null || ubicacion.getLongitud() == null) {
            throw new IllegalArgumentException("Latitud o longitud nulas en Ubicacion: " + ubicacion);
        }

        try {
            GeorefDireccionInputDTO georefDireccionDTO = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ubicacion")
                            .queryParam("lat", ubicacion.getLatitud())
                            .queryParam("lon", ubicacion.getLongitud())
                            .build())
                    .retrieve()
                    .bodyToMono(GeorefDireccionInputDTO.class)
                    .timeout(Duration.ofSeconds(10))
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

            ubicacion.setDepartamento(georefDireccionDTO.getDepartamento());
            return ubicacion;
        } catch (Exception e) {
            logger.error("Error obteniendo dirección para lat={}, lon={}: {}",
                    ubicacion.getLatitud(), ubicacion.getLongitud(), e.getMessage());
            ubicacion.setProvincia("DESCONOCIDA");
            ubicacion.setDepartamento(null);
            return ubicacion;
        }
    }

    public Ubicacion obtenerCoordenadas(Ubicacion ubicacion) {
        String direccion = buildDireccion(ubicacion.getCalle(), ubicacion.getNumero());

        try {
            GeorefCoordenadasInputDTO georefCoordenadasDTO = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/direcciones")
                            .queryParam("direccion", direccion)
                            .queryParam("provincia", ubicacion.getProvincia())
                            .queryParam("departamento", ubicacion.getDepartamento())
                            .queryParam("max", 1)
                            .build())
                    .retrieve()
                    .bodyToMono(GeorefCoordenadasInputDTO.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();

            if (georefCoordenadasDTO == null
                    || georefCoordenadasDTO.getLatitud(0) == null
                    || georefCoordenadasDTO.getLongitud(0) == null) {
                logger.warn("No se encontraron coordenadas para: {}, {}, {}",
                        direccion, ubicacion.getProvincia(), ubicacion.getDepartamento());
                return ubicacion;
            }

            ubicacion.setLatitud(georefCoordenadasDTO.getLatitud(0));
            ubicacion.setLongitud(georefCoordenadasDTO.getLongitud(0));
            return ubicacion;
        } catch (Exception e) {
            logger.error("Error obteniendo coordenadas para dirección {}: {}", direccion, e.getMessage());
            return ubicacion;
        }
    }

    /**
     * Normaliza el nombre de una provincia de forma síncrona con cache
     */
    private String normalizarProvinciaSincrono(String nombreProvincia) {
        if (nombreProvincia == null || nombreProvincia.isBlank()) {
            return "DESCONOCIDA";
        }

        // Verificar cache
        String cached = provinciasCache.get(nombreProvincia.toLowerCase().trim());
        if (cached != null) {
            return cached;
        }

        try {
            ProvinciasResponse response = this.webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/provincias")
                            .queryParam("nombre", nombreProvincia)
                            .queryParam("max", 1)
                            .queryParam("campos", "nombre")
                            .build())
                    .retrieve()
                    .bodyToMono(ProvinciasResponse.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();

            if (response != null && response.provincias != null && !response.provincias.isEmpty()) {
                String normalizado = response.provincias.get(0).nombre;
                // Guardar en cache
                provinciasCache.put(nombreProvincia.toLowerCase().trim(), normalizado);
                return normalizado;
            }
        } catch (Exception e) {
            logger.warn("Error normalizando provincia '{}': {}", nombreProvincia, e.getMessage());
        }

        // Si falla, devolver el original
        return nombreProvincia;
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

        logger.info("Iniciando geolocalización batch de {} ubicaciones", ubicaciones.size());

        // Primero normalizar provincias de forma reactiva
        return Flux.fromIterable(ubicaciones)
                .flatMap(this::normalizarProvinciaAsync)
                .collectList()
                .flatMap(this::geolocalizarBatchAsyncInternal)
                .doOnSuccess(result -> logger.info("Geolocalización batch completada: {} ubicaciones", result.size()))
                .doOnError(e -> logger.error("Error en geolocalización batch: {}", e.getMessage()));
    }

    /**
     * Normaliza la provincia de una ubicación de forma asíncrona
     */
    private Mono<Ubicacion> normalizarProvinciaAsync(Ubicacion ubicacion) {
        if (ubicacion.getProvincia() == null || ubicacion.getProvincia().isBlank()) {
            return Mono.just(ubicacion);
        }

        String key = ubicacion.getProvincia().toLowerCase().trim();

        // Verificar cache
        if (provinciasCache.containsKey(key)) {
            ubicacion.setProvincia(provinciasCache.get(key));
            return Mono.just(ubicacion);
        }

        // Normalizar via API
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/provincias")
                        .queryParam("nombre", ubicacion.getProvincia())
                        .queryParam("max", 1)
                        .queryParam("campos", "nombre")
                        .build())
                .retrieve()
                .bodyToMono(ProvinciasResponse.class)
                .timeout(Duration.ofSeconds(5))
                .map(response -> {
                    if (response.provincias != null && !response.provincias.isEmpty()) {
                        String normalizado = response.provincias.get(0).nombre;
                        provinciasCache.put(key, normalizado);
                        ubicacion.setProvincia(normalizado);
                    }
                    return ubicacion;
                })
                .onErrorReturn(ubicacion); // Si falla, devolver sin cambios
    }

    private Mono<List<Ubicacion>> geolocalizarBatchAsyncInternal(List<Ubicacion> ubicaciones) {
        // Configuración de concurrencia y reintentos más conservadora
        final int CONCURRENCIA = 2;
        final Duration PAUSA_ENTRE_CHUNKS = Duration.ofSeconds(1); // 1 segundo entre chunks
        final Retry RETRY_SUAVE = Retry.backoff(3, Duration.ofSeconds(2))
                .maxBackoff(Duration.ofSeconds(10))
                .jitter(0.5)
                .filter(ex -> ex instanceof WebClientResponseException w &&
                        (w.getStatusCode().value() == 429 || w.getStatusCode().is5xxServerError()));

        // Separar por casos
        List<Ubicacion> casoA = new ArrayList<>(); // direccion -> coords
        List<Ubicacion> casoB = new ArrayList<>(); // coords -> jurisdicciones
        List<Ubicacion> casoC = new ArrayList<>(); // completas

        for (Ubicacion u : ubicaciones) {
            if (u == null) continue;

            boolean tieneCoords = u.getLatitud() != null && u.getLongitud() != null;
            boolean tieneJurisd = u.getProvincia() != null && u.getDepartamento() != null;
            boolean tieneDirecc = u.getCalle() != null && u.getNumero() != null;

            if (!tieneCoords && tieneJurisd && tieneDirecc) {
                casoA.add(u);
            } else if (tieneCoords && !tieneJurisd) {
                casoB.add(u);
            } else {
                casoC.add(u);
            }
        }

        logger.info("Casos - A (dirección->coords): {}, B (coords->jurisdicción): {}, C (completas): {}",
                casoA.size(), casoB.size(), casoC.size());

        // Armar flujos con FLUX
        Flux<Ubicacion> completasFlux = Flux.fromIterable(casoC);

        Flux<Ubicacion> forwardFlux = chunk(casoA, BATCH_SIZE)
                .delayElements(PAUSA_ENTRE_CHUNKS)
                .flatMap(list -> callDireccionesBulk(list).retryWhen(RETRY_SUAVE), CONCURRENCIA)
                .onErrorContinue((ex, obj) -> {
                    logger.error("Error en /direcciones bulk: {}", ex.getMessage());
                });

        Flux<Ubicacion> reverseFlux = chunk(casoB, BATCH_SIZE)
                .delayElements(PAUSA_ENTRE_CHUNKS)
                .flatMap(list -> callUbicacionBulk(list).retryWhen(RETRY_SUAVE))
                .onErrorContinue((ex, obj) -> {
                    logger.error("Error en /ubicacion: {}", ex.getMessage());
                });

        return Flux.merge(completasFlux, forwardFlux, reverseFlux)
                .collectList()
                .map(updated -> ubicaciones); // Las ubicaciones se modifican in-place
    }

    // -----------------------------
    // Llamadas BULK reales (POST)
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
                    it.campos = "basico";
                    if (u.getProvincia() != null) it.provincia = u.getProvincia();
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
                .timeout(Duration.ofSeconds(30))
                .flatMapMany(resp -> mapDireccionesResponse(resp, lote))
                .onErrorResume(ex -> {
                    logger.error("Fallo /direcciones para {} ubicaciones: {}", lote.size(), ex.getMessage());
                    return Flux.fromIterable(lote);
                });
    }

    private Flux<Ubicacion> mapDireccionesResponse(DireccionesResponse resp, List<Ubicacion> originales) {
        if (resp == null || resp.resultados == null) return Flux.fromIterable(originales);

        List<Ubicacion> updated = new ArrayList<>();
        int idx = 0;

        for (DireccionesResponse.Result r : resp.resultados) {
            if (r.direcciones == null || r.direcciones.isEmpty()) {
                if (idx < originales.size()) {
                    logger.warn("No se encontraron resultados para: {}", originales.get(idx));
                    updated.add(originales.get(idx));
                    idx++;
                }
                continue;
            }

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
     * GET /ubicacion – georreferenciación inversa individual => set provincia/departamento
     * Con rate limiting para evitar 429 Too Many Requests
     */
    private Flux<Ubicacion> callUbicacionBulk(List<Ubicacion> lote) {
        if (lote == null || lote.isEmpty()) return Flux.empty();

        logger.info("Procesando {} ubicaciones con /ubicacion (GET individual)", lote.size());

        return Flux.fromIterable(lote)
                .delayElements(Duration.ofMillis(100)) // 100ms entre cada request = max 10 req/seg
                .flatMap(u -> {
                    if (u.getLatitud() == null || u.getLongitud() == null) {
                        u.setProvincia("DESCONOCIDA");
                        return Mono.just(u);
                    }

                    return webClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/ubicacion")
                                    .queryParam("lat", u.getLatitud())
                                    .queryParam("lon", u.getLongitud())
                                    .build())
                            .retrieve()
                            .bodyToMono(UbicacionGetResponse.class)
                            .timeout(Duration.ofSeconds(10))
                            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                                    .maxBackoff(Duration.ofSeconds(10))
                                    .filter(ex -> ex instanceof WebClientResponseException w &&
                                            w.getStatusCode().value() == 429))
                            .map(resp -> {
                                if (resp != null && resp.ubicacion != null) {
                                    if (resp.ubicacion.provincia != null && resp.ubicacion.provincia.nombre != null) {
                                        u.setProvincia(resp.ubicacion.provincia.nombre);
                                    } else {
                                        u.setProvincia("DESCONOCIDA");
                                    }

                                    if (resp.ubicacion.departamento != null && resp.ubicacion.departamento.nombre != null) {
                                        u.setDepartamento(resp.ubicacion.departamento.nombre);
                                    }
                                } else {
                                    u.setProvincia("DESCONOCIDA");
                                }
                                return u;
                            })
                            .onErrorResume(ex -> {
                                if (ex instanceof WebClientResponseException w && w.getStatusCode().value() == 429) {
                                    logger.warn("Rate limit alcanzado, seteando DESCONOCIDA para lat={}, lon={}",
                                            u.getLatitud(), u.getLongitud());
                                } else {
                                    logger.warn("Error obteniendo ubicación para lat={}, lon={}: {}",
                                            u.getLatitud(), u.getLongitud(), ex.getMessage());
                                }
                                u.setProvincia("DESCONOCIDA");
                                return Mono.just(u);
                            });
                }, 3); // Reducir concurrencia a 3 requests simultáneos
    }

    private Flux<Ubicacion> mapUbicacionResponse(UbicacionResponse resp, List<Ubicacion> originales) {
        // Este método ya no se usa - se eliminó el POST bulk
        return Flux.fromIterable(originales);
    }

    // ====== DTO para GET /ubicacion (singular) ======
    static class UbicacionGetResponse {
        public UbicacionData ubicacion;
        public Map<String, Object> parametros;

        static class UbicacionData {
            public Double lat;
            public Double lon;
            public Jurisdiccion provincia;
            public Jurisdiccion departamento;
            public Jurisdiccion municipio;
        }
    }

    // -----------------------------
    // Helpers
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

    // ====== DTOs /provincias (normalización) ======
    static class ProvinciasResponse {
        public List<Provincia> provincias;
        public Integer cantidad;
        public Integer total;

        static class Provincia {
            public String id;
            public String nombre;
        }
    }

    // ====== DTOs /direcciones ======
    static class DireccionesRequest {
        public List<Item> direcciones;

        static class Item {
            public String direccion;
            public Integer max;
            public String campos;
            public String provincia;
            public String departamento;
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
            public Calle calle;
            public String nomenclatura;
            public Punto ubicacion;
            public Jurisdiccion provincia;
            public Jurisdiccion departamento;
        }

        static class Altura {
            public Integer valor;
            public String unidad;
        }

        static class Calle {
            public String id;
            public String nombre;
            public String categoria;
        }

        static class Punto {
            public Double lat;
            public Double lon;
        }
    }

    // ====== DTOs /ubicacion ======
    static class UbicacionRequest {
        public List<Item> ubicaciones;
        public Boolean aplanar;

        static class Item {
            public Double lat;
            public Double lon;
            public String campos;
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