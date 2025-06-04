package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuenteHechos {
    Mono<List<HechoExternoDTO>> buscarTodos();
}
