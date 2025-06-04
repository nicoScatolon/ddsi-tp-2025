package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ColeccionInputDTO {

        private String id;
        private String titulo;
        private String descripcion;
        private List<HechoExternoDTO> hechos;

}
