package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaOutputDTO {
    private String id;
    private String nombreCategoria;
}
