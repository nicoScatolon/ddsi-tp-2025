package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ColeccionInputDTO {
        private String id;
        private String titulo;
        private String descripcion;

}
