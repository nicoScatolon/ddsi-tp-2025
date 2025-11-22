package ar.edu.utn.frba.dds.domain.entities.Geolocalizadores;

import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IGeoLocalizador {
    Ubicacion geolocalizar(Ubicacion ubicacion);

    Mono<List<Ubicacion>> geolocalizarBatchAsync(List<Ubicacion> ubicaciones);
    Ubicacion obtenerDireccionDesdeCache(Ubicacion ubicacion);}
