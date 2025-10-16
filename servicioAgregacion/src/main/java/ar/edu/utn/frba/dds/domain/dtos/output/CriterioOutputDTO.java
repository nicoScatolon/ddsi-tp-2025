package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriterioOutputDTO {
    private String tipo;
    private Map<String, String> parametros;
}
