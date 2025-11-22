package ar.edu.utn.frba.dds.domain.entities.Geolocalizadores;

import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.services.IGeorefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
public class Georef implements IGeoLocalizador {

    private static final Logger logger = LoggerFactory.getLogger(Georef.class);

    private final IGeorefService cacheService;

    public Georef(IGeorefService georefService) {
        this.cacheService = georefService;
    }

    //----------------------------- MÉTODOS SÍNCRONOS -----------------------------

    public Ubicacion geolocalizar(Ubicacion ubi) {
        if ((ubi.getLatitud() == null || ubi.getLongitud() == null)
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null) {
            // Caso A: normalizar provincia con cache
            String provinciaNormalizada = cacheService.buscarProvincia(ubi.getProvincia());
            ubi.setProvincia(provinciaNormalizada);
            return ubi;

        } else if ((ubi.getDepartamento() == null || ubi.getProvincia() == null)
                && ubi.getLatitud() != null
                && ubi.getLongitud() != null) {
            // Caso B: obtener dirección desde coordenadas usando cache
            return this.obtenerDireccionDesdeCache(ubi);

        } else if (ubi.getLatitud() != null
                && ubi.getLongitud() != null
                && ubi.getDepartamento() != null
                && ubi.getProvincia() != null
                && ubi.getCalle() != null
                && ubi.getNumero() != null) {
            // Caso C: ubicación completa
            return ubi;

        } else {
            throw new RuntimeException("La ubicación no es apta para geolocalizar");
        }
    }

    /**
     * Obtiene provincia y departamento desde coordenadas usando el CACHE LOCAL
     */
    public Ubicacion obtenerDireccionDesdeCache(Ubicacion ubicacion) {
        if (ubicacion.getLatitud() == null || ubicacion.getLongitud() == null) {
            ubicacion.setProvincia("DESCONOCIDA");
            return ubicacion;
        }

        Map<String, String> jurisdiccion = cacheService.buscarJurisdiccionPorCoordenadas(
                ubicacion.getLatitud(),
                ubicacion.getLongitud()
        );

        ubicacion.setProvincia(jurisdiccion.get("provincia"));
        ubicacion.setDepartamento(jurisdiccion.get("departamento"));

        logger.debug("Ubicación geolocalizada: lat={}, lon={} -> {}, {}",
                ubicacion.getLatitud(), ubicacion.getLongitud(),
                ubicacion.getProvincia(), ubicacion.getDepartamento());

        return ubicacion;
    }

    // ---------------------------------- MÉTODOS ASÍNCRONOS ----------------------------------

    public Mono<List<Ubicacion>> geolocalizarBatchAsync(List<Ubicacion> ubicaciones) {
        if (ubicaciones == null || ubicaciones.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }

        logger.info("Geolocalizando batch de {} ubicaciones usando CACHE LOCAL", ubicaciones.size());

        return Flux.fromIterable(ubicaciones)
                .flatMap(this::geolocalizarUbicacionAsync)
                .collectList()
                .doOnSuccess(result -> logger.info("✓ Batch completado: {} ubicaciones", result.size()))
                .doOnError(e -> logger.error("Error en batch: {}", e.getMessage()));
    }

    private Mono<Ubicacion> geolocalizarUbicacionAsync(Ubicacion ubicacion) {
        return Mono.fromCallable(() -> {
            // Caso A: normalizar provincia
            if (ubicacion.getProvincia() != null) {
                String provinciaNormalizada = cacheService.buscarProvincia(ubicacion.getProvincia());
                ubicacion.setProvincia(provinciaNormalizada);
            }

            // Caso B: obtener jurisdicción desde coordenadas
            if ((ubicacion.getProvincia() == null || ubicacion.getProvincia().equals("DESCONOCIDA"))
                    && ubicacion.getLatitud() != null
                    && ubicacion.getLongitud() != null) {
                obtenerDireccionDesdeCache(ubicacion);
            }

            return ubicacion;
        }).subscribeOn(reactor.core.scheduler.Schedulers.boundedElastic());
    }
}