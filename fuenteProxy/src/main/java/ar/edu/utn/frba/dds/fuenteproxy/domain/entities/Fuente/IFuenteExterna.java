package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

public interface IFuenteExterna extends IFuente {
    Mono<HechoExternoDTO> getHechoPorId(Long id);
}
