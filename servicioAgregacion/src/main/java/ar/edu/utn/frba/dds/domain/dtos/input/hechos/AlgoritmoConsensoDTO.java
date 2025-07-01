package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgoritmoConsensoDTO {
    private TipoAlgoritmoConsenso tipo;
}
