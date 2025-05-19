package ar.edu.utn.frba.dds.fuenteDinamica.services;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Usuario;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;

public interface ISolicitudesEliminacionService {
    SolicitudEliminacion crearSolicitud(SolicitudEliminacionInputDTO solicitudDTO);
    SolicitudEliminacion aceptarSolicitud(SolicitudEliminacionInputDTO solicitudDTO);
    SolicitudEliminacion rechazarSolicitud(SolicitudEliminacionInputDTO solicitudDTO);
    // TODO verificar si es necesario que me mande la solicitud entera o solamente el id y yo la busco (para aceptar y rechazar)
}
