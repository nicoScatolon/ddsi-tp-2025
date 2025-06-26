package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServidoraDeEliminaciones extends IFuente {
    Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud);
}
