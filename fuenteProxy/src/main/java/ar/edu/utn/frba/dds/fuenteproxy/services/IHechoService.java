package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechoService {
    Mono<List<HechoOutputDTO>> buscarTodos();
    Mono<HechoOutputDTO> buscarPorId(Long id);
}
