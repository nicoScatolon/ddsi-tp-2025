package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ColeccionInputDTO {
    private Set<CriterioInputDTO> listaCriterios;
    private Set<Long> listaIdsFuentes; //TODO cambiado, fijarse el crear colecciones como cambia
    private String handle;
    private String titulo;
    private String descripcion;
}