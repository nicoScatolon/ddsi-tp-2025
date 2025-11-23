package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PanelActividadViewDTO {

    // Provincia con más hechos en esa colección
    private String provinciaTop;
    private Integer hechosProvinciaTop;

    // Categoría con más hechos (global)
    private String categoriaTop;
    private Integer hechosCategoriaTop;

    // Solicitudes de eliminación (spam / total)
    private Integer solicitudesSpam;
    private Integer solicitudesTotales;
}
