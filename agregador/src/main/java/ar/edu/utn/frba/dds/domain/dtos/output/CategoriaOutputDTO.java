package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoriaOutputDTO {
    private String nombre;
    private Integer hash;
}
