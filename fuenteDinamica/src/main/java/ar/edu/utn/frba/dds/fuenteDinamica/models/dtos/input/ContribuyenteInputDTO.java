package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContribuyenteInputDTO {
    private Long id;
    private String nombre;
    private String apellido;
}
