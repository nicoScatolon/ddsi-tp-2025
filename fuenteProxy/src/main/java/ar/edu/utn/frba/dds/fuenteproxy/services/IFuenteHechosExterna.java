package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuenteHechosExterna{
    Mono<List<HechoExternoDTO>> buscarTodos();

}
