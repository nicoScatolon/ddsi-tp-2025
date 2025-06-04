package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

public interface IFuenteExterna extends IFuenteHechos {
    Mono<HechoExternoDTO> buscarPorId(Long id);
}
