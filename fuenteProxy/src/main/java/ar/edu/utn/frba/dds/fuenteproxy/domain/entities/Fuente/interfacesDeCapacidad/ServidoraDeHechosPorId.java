package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import reactor.core.publisher.Mono;

public interface ServidoraDeHechosPorId extends ServidoraDeHechos{
    Mono<HechoExternoDTO> getHechoPorId(Long id);
}
