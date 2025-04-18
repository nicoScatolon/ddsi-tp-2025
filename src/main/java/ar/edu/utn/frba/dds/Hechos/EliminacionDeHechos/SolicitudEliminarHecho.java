package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

//Como persona administradora, deseo poder aceptar o rechazar la solicitud de eliminación de un hecho. NICO

import ar.edu.utn.frba.dds.Hechos.Hecho;
import ar.edu.utn.frba.dds.Personas.Contribuyente;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SolicitudEliminarHecho {
    private Contribuyente contribuyente;
    private String razonDeEliminacion; //Deben estar adecuadamente fundamentadas
    private Hecho hecho;
    //ToDo: Incluir en una base de datos.


    public void serAceptada() {
        //ToDo:
        // - Se mantendrá en el sistema pero se elimina de las colecciones;
        // - Y no se puede incorporar a una fuente.
    }

    public void serRechazada(){
        //ToDo: Se eliminará por completo la solicitud.
    }
}



