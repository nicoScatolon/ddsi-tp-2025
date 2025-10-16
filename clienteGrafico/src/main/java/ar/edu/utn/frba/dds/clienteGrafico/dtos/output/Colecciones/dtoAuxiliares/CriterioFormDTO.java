package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CriterioFormDTO {
    private String tipo;
    private Map<String, String> parametros = new HashMap<>();
}
