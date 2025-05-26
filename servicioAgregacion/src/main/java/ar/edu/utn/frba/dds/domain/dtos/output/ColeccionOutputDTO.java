package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ColeccionOutputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private List<HechoOutputDTO> hechos;
}
