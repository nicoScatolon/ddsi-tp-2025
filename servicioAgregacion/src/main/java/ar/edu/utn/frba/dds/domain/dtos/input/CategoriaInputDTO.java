package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaInputDTO {
    private String id;
    private String nombre;
}
