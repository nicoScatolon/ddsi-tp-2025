package ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter
@Getter
public class SolicitudEliminarHecho {
    private Long id;
    private String razonDeEliminacion;
    private Hecho hecho;
    private EstadoDeSolicitud estado;
    private String nombreCreador;
    private String apellidoCreador;
    private String nombreAdministrador;
    private String apellidoAdministrador;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaAceptacion = null;
    private boolean eliminada = false;
    private Boolean actualizarFuenteOrigen = false; //el actualizar con la fuente de origen

    public void serAceptada(String nombreAdministrador, String apellidoAdministrador) {
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.ACEPTADA;
        fechaAceptacion = LocalDateTime.now();
        this.nombreAdministrador = nombreAdministrador;
        this.apellidoAdministrador = apellidoAdministrador;
        hecho.setFueEliminado(true);
        this.actualizarFuenteOrigen = true;
    }

    public void serRechazada(String nombreAdministrador, String apellidoAdministrador){
        if (estado != EstadoDeSolicitud.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue procesada.");
        }
        estado = EstadoDeSolicitud.RECHAZADA;
        this.nombreAdministrador = nombreAdministrador;
        this.apellidoAdministrador = apellidoAdministrador;
    }

}