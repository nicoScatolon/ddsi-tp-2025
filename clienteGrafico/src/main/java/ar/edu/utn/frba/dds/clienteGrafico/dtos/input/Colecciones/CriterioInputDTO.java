package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriterioInputDTO {
    private String tipo;
    private Map<String, String> parametros;
}
