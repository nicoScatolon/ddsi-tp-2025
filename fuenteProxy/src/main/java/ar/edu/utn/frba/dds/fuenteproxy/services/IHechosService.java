package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {
    Mono<List<HechoOutputDTO>> buscarTodos();
}
