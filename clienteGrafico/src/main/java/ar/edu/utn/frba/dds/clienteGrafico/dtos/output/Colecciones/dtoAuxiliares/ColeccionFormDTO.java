package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ColeccionFormDTO {
    private List<CriterioFormDTO> listaCriterios = new ArrayList<>();
    private List<Long> listaIdsFuentes = new ArrayList<>();
    private String handle;
    private String titulo;
    private String descripcion;
    private String algoritmoConsensoTipo;

    public String getListaCriteriosJson() {
        if (listaCriterios == null || listaCriterios.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < listaCriterios.size(); i++) {
            CriterioFormDTO c = listaCriterios.get(i);
            if (i > 0) json.append(",");

            json.append("{\"tipo\":\"").append(escaparJson(c.getTipo())).append("\"");

            if (c.getParametros() != null && !c.getParametros().isEmpty()) {
                json.append(",\"parametros\":{");
                int j = 0;
                for (Map.Entry<String, String> entry : c.getParametros().entrySet()) {
                    if (j > 0) json.append(",");
                    json.append("\"").append(escaparJson(entry.getKey())).append("\":\"")
                            .append(escaparJson(entry.getValue())).append("\"");
                    j++;
                }
                json.append("}");
            }
            json.append("}");
        }
        json.append("]");
        return json.toString();
    }

    private String escaparJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }
}