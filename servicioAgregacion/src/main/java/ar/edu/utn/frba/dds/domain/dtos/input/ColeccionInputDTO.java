package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionInputDTO {
    private Set<CriterioInputDTO> listaCriterios;
    private Set<Long> listaIdsFuentes;
    private String handle;
    private String titulo;
    private String descripcion;
    private AlgoritmoConsensoDTO algoritmoConsenso;
}