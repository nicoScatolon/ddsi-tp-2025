package ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Usuario;
import java.time.LocalDateTime;

public class ConstructorSolicitudesEliminacion {
    private static final Integer CARACTERES_MINIMOS_RAZON_ELIMINACION = 500;

    public static SolicitudEliminacion constructorSolicitud(Hecho hecho, String razonDeEliminacion, Usuario contribuyente) {
        if(razonDeEliminacion.length() < CARACTERES_MINIMOS_RAZON_ELIMINACION){
            throw new IllegalArgumentException("La razón de eliminación debe tener al menos " + CARACTERES_MINIMOS_RAZON_ELIMINACION + "caracteres.");
        }

        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        solicitud.setHecho(hecho);
        solicitud.setRazonDeEliminacion(razonDeEliminacion);
        solicitud.setEstado(EstadoSolicitudEliminacion.PENDIENTE);
        solicitud.setCreador(contribuyente);
        solicitud.setFechaCreacion(LocalDateTime.now());

        return solicitud;
    }
}
