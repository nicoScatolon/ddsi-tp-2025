package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.dtoAuxiliares;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ColeccionFormDTO {
    private List<CriterioFormDTO> listaCriterios = new ArrayList<>();
    private List<Long> listaIdsFuentes = new ArrayList<>();
    private String handle;
    private String titulo;
    private String descripcion;
    private String algoritmoConsensoTipo;
}
