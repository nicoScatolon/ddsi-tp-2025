package ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion;

import ar.edu.utn.frba.dds.domain.entities.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConstructorSolicitudesEliminacion {
    private static final Integer CARACTERES_MINIMOS_RAZON_ELIMINACION = 500;

    public static SolicitudEliminarHecho constructorSolicitud(Hecho hecho
                                                            , String razonDeEliminacion, String nombreContribuyente, String apellidoContribuyente) {
        if(razonDeEliminacion.length() < CARACTERES_MINIMOS_RAZON_ELIMINACION){
            throw new IllegalArgumentException("La razón de eliminación debe tener al menos " + CARACTERES_MINIMOS_RAZON_ELIMINACION + "caracteres.");
        }

        SolicitudEliminarHecho solicitud = new SolicitudEliminarHecho();
        solicitud.setHecho(hecho);
        solicitud.setRazonDeEliminacion(razonDeEliminacion);
        solicitud.setEstado(EstadoDeSolicitud.PENDIENTE);
        solicitud.setNombreCreador(nombreContribuyente);
        solicitud.setApellidoCreador(apellidoContribuyente);
        solicitud.setFechaCreacion(LocalDateTime.now());


        return solicitud;
    }
}