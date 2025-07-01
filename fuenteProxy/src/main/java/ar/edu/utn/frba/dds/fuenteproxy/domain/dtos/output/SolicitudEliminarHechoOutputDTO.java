package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class SolicitudEliminarHechoOutputDTO {
        private Long idHecho;
        private String razonDeEliminacion;
        private LocalDateTime fechaCreacion;
        private String nombreCreador;
        private String apellidoCreador;


}
