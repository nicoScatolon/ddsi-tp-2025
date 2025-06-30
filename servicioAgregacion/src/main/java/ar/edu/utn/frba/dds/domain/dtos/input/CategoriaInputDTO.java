package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaInputDTO {
    private String id;
    private String nombre;
}
