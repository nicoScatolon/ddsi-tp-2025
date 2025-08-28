package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ICollecionesService {
    Mono<List<ColeccionInputDTO>> traerTodasLasColecciones();
    Mono<List<HechoOutputDTO>> traerHechosDeColeccion(String handleColeccion);
}
