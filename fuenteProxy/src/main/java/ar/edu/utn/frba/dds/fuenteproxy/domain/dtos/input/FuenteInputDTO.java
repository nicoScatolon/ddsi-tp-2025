package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuenteInputDTO {
    private String nombre;
    private String baseUrl;
}
