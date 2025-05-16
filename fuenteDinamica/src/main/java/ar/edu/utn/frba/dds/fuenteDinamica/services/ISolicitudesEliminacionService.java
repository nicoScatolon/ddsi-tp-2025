package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Usuario;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;

public interface ISolicitudesEliminacionService {
    SolicitudEliminacion crearSolicitud(SolicitudEliminacion solicitudDTO);

    SolicitudEliminacion aceptarSolicitud(SolicitudEliminacion solicitudDTO, Usuario adminDTO);
    SolicitudEliminacion rechazarSolicitud(SolicitudEliminacion solicitudDTO, Usuario adminDTO);
    // TODO verificar si es necesario que me mande la solicitud entera o solamente el id y yo la busco (para aceptar y rechazar)
}
