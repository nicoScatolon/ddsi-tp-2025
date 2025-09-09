package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuenteOutputDTO {
    private Long id;
    private String nombre;
    private String tipo;
}
