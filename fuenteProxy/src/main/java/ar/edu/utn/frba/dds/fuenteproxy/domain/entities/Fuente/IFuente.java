package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuente {
    Long getId();
    void setId(Long id);
    TipoFuenteProxy getTipoFuenteProxy();

}
