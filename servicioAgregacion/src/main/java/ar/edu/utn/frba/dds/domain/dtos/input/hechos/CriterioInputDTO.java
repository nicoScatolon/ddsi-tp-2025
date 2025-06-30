package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CriterioInputDTO {
    private String tipo;
    private Map<String, String> parametros;
}
