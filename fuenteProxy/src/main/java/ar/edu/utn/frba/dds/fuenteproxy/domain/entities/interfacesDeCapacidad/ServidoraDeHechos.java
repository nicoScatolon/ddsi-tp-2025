package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServidoraDeHechos extends IFuente {
    Mono<List<HechoInputDTO>> getHechos();
}
