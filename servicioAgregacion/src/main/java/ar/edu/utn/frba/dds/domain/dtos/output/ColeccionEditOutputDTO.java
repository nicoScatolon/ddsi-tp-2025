package ar.edu.utn.frba.dds.domain.dtos.output;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.TipoAlgoritmoConsenso;
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
public class ColeccionEditOutputDTO {
    private List<FuentePreviewOutputDTO> fuentes;
    private Set<CriterioOutputDTO> listaCriterios;
    private String handle;
    private String titulo;
    private String descripcion;
    private TipoAlgoritmoConsenso algoritmoConsenso;
}