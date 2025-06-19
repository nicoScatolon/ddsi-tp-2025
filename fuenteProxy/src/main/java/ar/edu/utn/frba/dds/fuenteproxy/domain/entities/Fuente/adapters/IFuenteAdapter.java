package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuenteAdapter {
    void setFuente(IFuente fuente);
    Mono<List<HechoExternoDTO>> obtenerHechos();
    Long getId();
    TipoFuenteProxy getTipo();
}
