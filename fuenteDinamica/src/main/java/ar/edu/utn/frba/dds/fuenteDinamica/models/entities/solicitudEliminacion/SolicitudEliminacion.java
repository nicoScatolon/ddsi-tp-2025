package ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Usuario;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class SolicitudEliminacion {
    private Long id;

    private Hecho hecho;
    private String razonDeEliminacion;
    private Usuario creador;
    private Usuario administrador;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaGestion = null;
    private EstadoSolicitudEliminacion estado;

    public void serAceptada(Usuario administrador) {
        this.fechaGestion = LocalDateTime.now();
        this.estado = EstadoSolicitudEliminacion.ACEPTADA;
        this.administrador = administrador;
        this.hecho.setFueEliminado(true);
    }

    public void serRechazada(Usuario administrador){
        estado = EstadoSolicitudEliminacion.RECHAZADA;
        this.administrador = administrador;
        //ToDo: Se eliminará por completo la solicitud.
    }
}
