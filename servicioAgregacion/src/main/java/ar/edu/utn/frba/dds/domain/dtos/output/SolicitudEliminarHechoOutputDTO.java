package ar.edu.utn.frba.dds.domain.dtos.output;

import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SolicitudEliminarHechoOutputDTO {
    private Long id;
    private String razonDeEliminacion;
    private IHecho hecho; //ToDo: deberia importar el hecho?? O deberia apuntar a un hecho existente, si es asi como??
    private LocalDateTime fechaCreacion;
    private String nombreCreador;
    private String apellidoCreador;
}





