package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import reactor.core.publisher.Mono;

public interface ServidoraDeHechosPorId extends ServidoraDeHechos{
    Mono<HechoInputDTO> getHechoPorId(Long id);
}
