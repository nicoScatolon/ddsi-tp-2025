package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import reactor.core.publisher.Mono;

public interface ISolicitudesEliminacionService {
    Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud);
}
