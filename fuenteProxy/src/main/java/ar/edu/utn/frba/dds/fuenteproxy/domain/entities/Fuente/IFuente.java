package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuente {
    Mono<List<HechoExternoDTO>> getHechos();
    String getId();
    void setId(Long id);
    TipoFuenteProxy getTipoFuenteProxy();
}
