package ar.edu.utn.frba.dds.Hechos.EliminacionDeHechos;

//Como persona administradora, deseo poder aceptar o rechazar la solicitud de eliminación de un hecho.

import ar.edu.utn.frba.dds.Hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
public class SolicitudEliminarHecho {
    private String razonDeEliminacion; //Deben estar adecuadamente fundamentadas
    private Hecho hecho;
    private EstadoDeSolicitud estado;
    private String nombreCreador;
    private String apellidoCreador;
    private String nombreAdministrador;
    private String apellidoAdministrador;
    private LocalDate fechaCreacion;
    private LocalDate fechaAceptacion = null;



    public void serAceptada(String nombreAdministrador, String apellidoAdministrador) {
        estado = EstadoDeSolicitud.ACEPTADA;
        fechaAceptacion = LocalDate.now();
        this.nombreAdministrador = nombreAdministrador;
        hecho.setFueEliminado(true);
    }

    public void serRechazada(String nombreAdministrador, String apellidoAdministrador){
        estado = EstadoDeSolicitud.RECHAZADA;
        this.nombreAdministrador = nombreAdministrador;
    }
}