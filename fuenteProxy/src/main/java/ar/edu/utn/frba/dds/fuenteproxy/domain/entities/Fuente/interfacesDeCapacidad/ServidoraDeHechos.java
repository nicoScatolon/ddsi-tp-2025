package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServidoraDeHechos extends IFuente {
    Mono<List<HechoExternoDTO>> getHechos();
}
