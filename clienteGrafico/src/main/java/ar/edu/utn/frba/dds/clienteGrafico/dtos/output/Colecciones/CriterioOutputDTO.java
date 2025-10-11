package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CriterioOutputDTO {
    private String tipo;
    private Map<String, String> parametros = new HashMap<>();
}
