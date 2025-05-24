package ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion;

import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class SolicitudEliminarHecho {
    private Long id;
    private String razonDeEliminacion; //Deben estar adecuadamente fundamentadas
    private IHecho hecho;
    private EstadoDeSolicitud estado;
    private String nombreCreador;
    private String apellidoCreador;
    private String nombreAdministrador;
    private String apellidoAdministrador;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAceptacion = null;


    public void serAceptada(String nombreAdministrador, String apellidoAdministrador) {
        estado = EstadoDeSolicitud.ACEPTADA;
        fechaAceptacion = LocalDateTime.now();
        this.nombreAdministrador = nombreAdministrador;
        hecho.setFueEliminado(true);
    }

    public void serRechazada(String nombreAdministrador, String apellidoAdministrador){
        estado = EstadoDeSolicitud.RECHAZADA;
        this.nombreAdministrador = nombreAdministrador;
        //ToDo: Se eliminará por completo la solicitud.
    }
}