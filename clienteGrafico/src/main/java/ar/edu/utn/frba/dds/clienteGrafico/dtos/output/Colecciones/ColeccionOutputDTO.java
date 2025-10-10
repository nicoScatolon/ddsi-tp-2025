package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.AlgoritmoConsensoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ColeccionOutputDTO {
    private Set<CriterioOutputDTO> listaCriterios;
    private Set<Long> listaIdsFuentes;
    private String handle;
    private String titulo;
    private String descripcion;
    private AlgoritmoConsensoDTO algoritmoConsenso;
}
