package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ColeccionInputDTO {
    private Set<CriterioInterfaz> listaCriterios;
    private String handle;
    private String titulo;
    private String descripcion;
}