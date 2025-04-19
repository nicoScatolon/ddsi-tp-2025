package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

import ar.edu.utn.frba.dds.Hechos.Hecho;

public class ConstructorSolicitudesEliminacion {
    private static final Integer CARACTERES_MINIMOS_RAZON_ELIMINACION = 500;

    public static SolicitudEliminarHecho constructorSolicitud(Hecho hecho, String razonDeEliminacion) {
        if(razonDeEliminacion.length() < CARACTERES_MINIMOS_RAZON_ELIMINACION){
            throw new IllegalArgumentException("La razón de eliminación debe tener al menos " + CARACTERES_MINIMOS_RAZON_ELIMINACION + "caracteres.");
        }
        SolicitudEliminarHecho solicitud = new SolicitudEliminarHecho();
        solicitud.setHecho(hecho);
        solicitud.setRazonDeEliminacion(razonDeEliminacion);
        solicitud.setEstado(EstadoDeSolicitud.PENDIENTE);

        return solicitud;
    }
}

//ToDo: Duda, se podría usar el Pattern State?? Ya que cambia

