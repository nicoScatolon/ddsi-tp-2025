package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

//Como persona administradora, deseo poder aceptar o rechazar la solicitud de eliminación de un hecho.

import ar.edu.utn.frba.dds.Hechos.Hecho;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
public class SolicitudEliminarHecho {
    private String razonDeEliminacion; //Deben estar adecuadamente fundamentadas
    private Hecho hecho;
    private EstadoDeSolicitud estado;
    //ToDo: Incluir en una base de datos.


    public void serAceptada() {
        estado = EstadoDeSolicitud.ACEPTADA;
        hecho.setFueEliminado(true);
    }

    public void serRechazada(){
        estado = EstadoDeSolicitud.RECHAZADA;
        //ToDo: Se eliminará por completo la solicitud.
    }
}



