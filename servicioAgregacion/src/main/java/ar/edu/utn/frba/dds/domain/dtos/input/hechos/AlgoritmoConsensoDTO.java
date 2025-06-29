package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AlgoritmoConsensoDTO {
    private String tipo;
    private Map<String,String> parametros;
}
