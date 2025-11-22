package ar.edu.utn.frba.dds.services.impl;


import ar.edu.utn.frba.dds.services.IGeorefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class GeorefService implements IGeorefService {

    private static final Logger logger = LoggerFactory.getLogger(GeorefService.class);
    private static final String BASE_URL = "https://apis.datos.gob.ar/georef/api";

    private final WebClient webClient;

    // Cache en memoria
    private final Map<String, ProvinciaCache> provinciasCache = new ConcurrentHashMap<>();
    private final Map<String, ProvinciaCache> provinciasPorId = new ConcurrentHashMap<>();
    private final Map<String, DepartamentoCache> departamentosCache = new ConcurrentHashMap<>();
    private final List<DepartamentoCache> departamentosLista = new ArrayList<>();

    private boolean cacheInicializado = false;

    public GeorefService() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .build();
        this.inicializarCache();
    }

    /**
     * Inicializa el cache descargando TODOS los datos de Georef
     */
    public void inicializarCache() {
        if (cacheInicializado) {
            logger.info("Cache ya inicializado");
            return;
        }

        logger.info("=== INICIANDO DESCARGA DEL CATÁLOGO GEOREF ===");
        long inicio = System.currentTimeMillis();

        try {
            // 1. Descargar provincias
            descargarProvincias();

            // 2. Descargar departamentos
            descargarDepartamentos();

            cacheInicializado = true;
            long duracion = System.currentTimeMillis() - inicio;

            logger.info("=== CACHE GEOREF INICIALIZADO ===");
            logger.info("Tiempo: {}ms", duracion);
            logger.info("Provincias: {}", provinciasCache.size());
            logger.info("Departamentos: {}", departamentosLista.size());
            logger.info("================================");

        } catch (Exception e) {
            logger.error("ERROR CRÍTICO inicializando cache Georef: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo inicializar el cache de Georef", e);
        }
    }

    private void descargarProvincias() {
        try {
            logger.info("Descargando provincias...");

            ProvinciasResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/provincias")
                            .queryParam("campos", "completo")
                            .queryParam("max", 100)
                            .build())
                    .retrieve()
                    .bodyToMono(ProvinciasResponse.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();

            if (response != null && response.provincias != null) {
                for (ProvinciasResponse.Provincia p : response.provincias) {
                    ProvinciaCache pc = new ProvinciaCache();
                    pc.id = p.id;
                    pc.nombre = p.nombre;
                    pc.nombreNormalizado = normalizarTexto(p.nombre);

                    // Guardar por nombre normalizado
                    provinciasCache.put(pc.nombreNormalizado, pc);
                    // Guardar por ID
                    provinciasPorId.put(p.id, pc);

                    logger.debug("Provincia cargada: {} -> {}", p.nombre, pc.nombreNormalizado);
                }
                logger.info("✓ {} provincias descargadas", response.provincias.size());
            }
        } catch (Exception e) {
            logger.error("Error descargando provincias: {}", e.getMessage());
            throw e;
        }
    }

    private void descargarDepartamentos() {
        try {
            logger.info("Descargando departamentos...");

            DepartamentosResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/departamentos")
                            .queryParam("campos", "completo")
                            .queryParam("max", 1000)
                            .build())
                    .retrieve()
                    .bodyToMono(DepartamentosResponse.class)
                    .timeout(Duration.ofSeconds(60))
                    .block();

            if (response != null && response.departamentos != null) {
                for (DepartamentosResponse.Departamento d : response.departamentos) {
                    DepartamentoCache dc = new DepartamentoCache();
                    dc.id = d.id;
                    dc.nombre = d.nombre;
                    dc.nombreNormalizado = normalizarTexto(d.nombre);
                    dc.provinciaId = d.provincia != null ? d.provincia.id : null;
                    dc.provinciaNombre = d.provincia != null ? d.provincia.nombre : null;

                    if (d.centroide != null) {
                        dc.centroideLat = d.centroide.lat;
                        dc.centroideLon = d.centroide.lon;
                    }

                    // Guardar en mapa con key compuesta
                    String key = dc.nombreNormalizado + "_" + dc.provinciaId;
                    departamentosCache.put(key, dc);

                    // También en lista para búsqueda por coordenadas
                    departamentosLista.add(dc);

                    logger.debug("Departamento: {} ({}) - lat={}, lon={}",
                            d.nombre, dc.provinciaNombre, dc.centroideLat, dc.centroideLon);
                }
                logger.info("✓ {} departamentos descargados", response.departamentos.size());
            }
        } catch (Exception e) {
            logger.error("Error descargando departamentos: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Busca una provincia normalizada
     */
    public String buscarProvincia(String nombreProvincia) {
        if (nombreProvincia == null || nombreProvincia.isBlank()) {
            return "DESCONOCIDA";
        }

        String normalizado = normalizarTexto(nombreProvincia);
        ProvinciaCache provincia = provinciasCache.get(normalizado);

        if (provincia != null) {
            logger.debug("Provincia encontrada exacta: {} -> {}", nombreProvincia, provincia.nombre);
            return provincia.nombre;
        }

        // Búsqueda fuzzy
        for (ProvinciaCache p : provinciasCache.values()) {
            if (p.nombreNormalizado.contains(normalizado) || normalizado.contains(p.nombreNormalizado)) {
                logger.debug("Provincia encontrada fuzzy: {} -> {}", nombreProvincia, p.nombre);
                return p.nombre;
            }
        }

        logger.warn("Provincia NO encontrada: {} (normalizado: {})", nombreProvincia, normalizado);
        return nombreProvincia;
    }

    /**
     * Busca jurisdicción por coordenadas
     */
    public Map<String, String> buscarJurisdiccionPorCoordenadas(Double lat, Double lon) {
        Map<String, String> resultado = new HashMap<>();
        resultado.put("provincia", "DESCONOCIDA");
        resultado.put("departamento", null);

        if (lat == null || lon == null) {
            logger.warn("Coordenadas nulas, retornando DESCONOCIDA");
            return resultado;
        }

        logger.debug("Buscando jurisdicción para coordenadas: lat={}, lon={}", lat, lon);

        DepartamentoCache masProximo = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (DepartamentoCache dept : departamentosLista) {
            if (dept.centroideLat != null && dept.centroideLon != null) {
                double distancia = calcularDistancia(lat, lon, dept.centroideLat, dept.centroideLon);

                if (distancia < distanciaMinima) {
                    distanciaMinima = distancia;
                    masProximo = dept;
                }
            }
        }

        // Umbral de 1 grado ≈ 111km (más generoso)
        if (masProximo != null && distanciaMinima < 1.0) {
            resultado.put("provincia", masProximo.provinciaNombre);
            resultado.put("departamento", masProximo.nombre);
            logger.debug("Jurisdicción encontrada: {} - {} (distancia: {})",
                    masProximo.provinciaNombre, masProximo.nombre, distanciaMinima);
        } else {
            logger.warn("No se encontró jurisdicción cercana para lat={}, lon={} (distancia mínima: {})",
                    lat, lon, distanciaMinima);
        }

        return resultado;
    }

    /**
     * Calcula distancia euclidiana entre dos puntos
     */
    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

    /**
     * Normaliza texto: lowercase, sin acentos, sin espacios extra
     */
    private String normalizarTexto(String texto) {
        if (texto == null) return "";

        return texto.toLowerCase()
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u").replace("ñ", "n")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public boolean isCacheInicializado() {
        return cacheInicializado;
    }

    // ===== CLASES INTERNAS DE CACHE =====

    static class ProvinciaCache {
        String id;
        String nombre;
        String nombreNormalizado;
    }

    static class DepartamentoCache {
        String id;
        String nombre;
        String nombreNormalizado;
        String provinciaId;
        String provinciaNombre;
        Double centroideLat;
        Double centroideLon;
    }

    // ===== DTOs DE GEOREF =====

    static class ProvinciasResponse {
        public List<Provincia> provincias;

        static class Provincia {
            public String id;
            public String nombre;
        }
    }

    static class DepartamentosResponse {
        public List<Departamento> departamentos;

        static class Departamento {
            public String id;
            public String nombre;
            public Provincia provincia;
            public Centroide centroide;
        }

        static class Provincia {
            public String id;
            public String nombre;
        }

        static class Centroide {
            public Double lat;
            public Double lon;
        }
    }
}