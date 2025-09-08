package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import reactor.core.publisher.Mono;


import java.util.List;

public interface ServidoraDeHechosConFiltros extends ServidoraDeHechos{
    Mono<List<HechoInputDTO>> buscarHechos(HechosFilterDTO filtros);
}
