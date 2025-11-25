package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuenteInputDTO {
    private String nombre;
    @JsonAlias({"baseUrl", "urlBase"})
    private String baseUrl;
}
