package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CriterioFormDTO {
    private String tipo;
    private Map<String, String> parametros = new HashMap<>();

    public List<String> getEtiquetasAsList() {
        String etiquetasJson = parametros.get("etiquetas");
        if (etiquetasJson == null || etiquetasJson.isEmpty()) {
            return Collections.emptyList();
        }

        // Si viene como JSON array
        if (etiquetasJson.startsWith("[")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(etiquetasJson,
                        new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                // Si falla el parseo, retornar lista vacía
                return Collections.emptyList();
            }
        }

        // Si viene como string simple (fallback)
        return Collections.singletonList(etiquetasJson);
    }
}
