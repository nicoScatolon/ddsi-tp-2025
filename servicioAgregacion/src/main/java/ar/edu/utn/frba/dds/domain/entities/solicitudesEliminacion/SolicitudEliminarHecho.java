package ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion;

import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class SolicitudEliminarHecho {
    private Long id;
    private String razonDeEliminacion;
    private IHecho hecho;
    private EstadoDeSolicitud estado;
    private String nombreCreador;
    private String apellidoCreador;
    private String nombreAdministrador;
    private String apellidoAdministrador;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAceptacion = null;
    private boolean eliminada = false;


    public void serAceptada(String nombreAdministrador, String apellidoAdministrador) {
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.ACEPTADA;
        fechaAceptacion = LocalDateTime.now();
        this.nombreAdministrador = nombreAdministrador;
        this.apellidoAdministrador = apellidoAdministrador;
        hecho.setFueEliminado(true);
    }

    public void serRechazada(String nombreAdministrador, String apellidoAdministrador){
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.RECHAZADA;
        this.nombreAdministrador = nombreAdministrador;
        this.apellidoAdministrador = apellidoAdministrador;
    }

    public void rechazarAutomaticamente(){
        this.serRechazada("Rechazada","Automaticamente");
    }
}