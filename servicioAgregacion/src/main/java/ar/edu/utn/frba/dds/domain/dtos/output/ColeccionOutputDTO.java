package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ColeccionOutputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
}
