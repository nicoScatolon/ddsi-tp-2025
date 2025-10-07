package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class SolicitudEliminarHechoInputDTO {
        private Long idHecho;
        private String razonDeEliminacion;
        private LocalDateTime fechaCreacion;
        private String nombreCreador;
        private String apellidoCreador;


}
