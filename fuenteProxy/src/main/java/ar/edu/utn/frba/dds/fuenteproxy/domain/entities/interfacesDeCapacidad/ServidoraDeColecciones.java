package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;


import java.util.List;

public interface ServidoraDeColecciones extends IFuente {
    Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones();
    Mono<List<HechoInputDTO>> buscarHechosPorColeccion(String handle, Boolean curado, HechosFilterDTO filtros);
}
