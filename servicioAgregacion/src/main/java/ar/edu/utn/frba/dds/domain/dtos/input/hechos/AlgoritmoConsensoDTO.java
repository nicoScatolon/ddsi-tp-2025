package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.TipoAlgoritmoConsenso;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgoritmoConsensoDTO {
    private TipoAlgoritmoConsenso tipo;
}
