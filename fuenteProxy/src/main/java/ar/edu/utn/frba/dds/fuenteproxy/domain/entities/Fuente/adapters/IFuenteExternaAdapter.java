package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

public interface IFuenteExternaAdapter extends IFuenteAdapter {
    Mono<HechoExternoDTO> obtenerPorId(Long id);
}
