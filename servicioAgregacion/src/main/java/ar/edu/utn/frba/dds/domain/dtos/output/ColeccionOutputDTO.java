package ar.edu.utn.frba.dds.domain.dtos.output;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionOutputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private AlgoritmoConsensoDTO algoritmoConsenso;
    private List<HechoOutputDTO> hechos;
}
