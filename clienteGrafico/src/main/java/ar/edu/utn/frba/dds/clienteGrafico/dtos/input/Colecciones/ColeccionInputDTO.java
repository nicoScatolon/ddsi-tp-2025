package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.CriterioOutputDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColeccionInputDTO {
    private List<FuenteInputDTO> fuentes;
    private Set<CriterioInputDTO> listaCriterios;
    private String handle;
    private String titulo;
    private String descripcion;
    private TipoAlgoritmoConsenso algoritmoConsenso;
}
