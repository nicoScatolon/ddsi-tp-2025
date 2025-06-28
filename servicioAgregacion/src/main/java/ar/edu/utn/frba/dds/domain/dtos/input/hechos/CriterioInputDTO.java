package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CriterioInputDTO {
    private String tipo;
    private Map<String, String> parametros;
}
