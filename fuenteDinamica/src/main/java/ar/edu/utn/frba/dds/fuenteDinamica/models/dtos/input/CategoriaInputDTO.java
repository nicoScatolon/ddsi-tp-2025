package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;




import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaInputDTO {
    private String id;
    private String nombreCategoria;
}
