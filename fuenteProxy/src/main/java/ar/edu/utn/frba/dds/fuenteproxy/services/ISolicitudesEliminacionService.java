package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import reactor.core.publisher.Mono;

public interface ISolicitudesEliminacionService {
    Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoInputDTO solicitud);
}
