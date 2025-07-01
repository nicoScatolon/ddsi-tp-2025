package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;

public interface ServidoraDeEliminaciones extends IFuente {
    Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud);
}
