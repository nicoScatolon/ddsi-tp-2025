package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Personas.Contribuyente;

public class ConstructorSolicitudes {
    private Contribuyente contribuyente;
    private String razonDeEliminacion;
    private Hecho hecho;
    private EstadoDeSolicitud estado;
    private static final Integer CARACTERES_MINIMOS_RAZON_ELIMINACION = 500;

    //CONSTRUCTORES
    public ConstructorSolicitudes sumarContribuyente(Contribuyente newContribuyente) {
        contribuyente = newContribuyente;
        return this;
    }

    public ConstructorSolicitudes sumarHecho(Hecho newHecho) {
        hecho = newHecho;
        return this;
    }

    public ConstructorSolicitudes sumarRazonDeEliminacion(String newRazonDeEliminacion) {
        razonDeEliminacion = newRazonDeEliminacion;
        return this;
    }

    public SolicitudEliminarHecho constructor() {
        if(razonDeEliminacion.length() < CARACTERES_MINIMOS_RAZON_ELIMINACION){
            throw new IllegalArgumentException("La razón de eliminación debe tener al menos " + CARACTERES_MINIMOS_RAZON_ELIMINACION + "caracteres.");
        }

        SolicitudEliminarHecho solicitud = new SolicitudEliminarHecho();
        solicitud.setContribuyente(contribuyente);
        solicitud.setHecho(hecho);
        solicitud.setRazonDeEliminacion(razonDeEliminacion);

        estado = EstadoDeSolicitud.PENDIENTE;
        //ToDo se debe agregar a la base de datos de solicitudes pendientes

        return solicitud;

    }
}

//ToDo: Duda, se podría usar el Pattern State?? Ya que cambia

