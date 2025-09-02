package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
public class SolicitudEliminacion {
    private Hecho hecho;
    private String razonDeEliminacion;
    private Contribuyente creador;
    private LocalDateTime fechaCreacion;

    private Long id;
    private Long idAdmin;
    private LocalDateTime fechaGestion = null;
    private EstadoSolicitudEliminacion estado;
    //TODO consultar que hacer con las solicitudes de eliminacion, ya que las guardamos en el agregador, si aca son necesarias o que
}
