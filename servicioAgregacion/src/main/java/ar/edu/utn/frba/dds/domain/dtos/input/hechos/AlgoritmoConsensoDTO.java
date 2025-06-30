package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.TipoAlgoritmoConsenso;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class AlgoritmoConsensoDTO {
    private TipoAlgoritmoConsenso tipo;
    private Map<String,String> parametros;
}
