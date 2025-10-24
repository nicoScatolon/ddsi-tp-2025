package ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDateTime;

public class ConstructorSolicitudesEliminacion {

    public static SolicitudEliminarHecho constructorSolicitud (Hecho hecho, String razonDeEliminacion, Long idCreador) {

        SolicitudEliminarHecho solicitud = new SolicitudEliminarHecho();
        solicitud.setHecho(hecho);
        solicitud.setRazonDeEliminacion(razonDeEliminacion);
        solicitud.setEstado(EstadoDeSolicitud.PENDIENTE);
        solicitud.setIdCreador(idCreador);
        solicitud.setFechaCreacion(LocalDateTime.now());

        return solicitud;
    }
}