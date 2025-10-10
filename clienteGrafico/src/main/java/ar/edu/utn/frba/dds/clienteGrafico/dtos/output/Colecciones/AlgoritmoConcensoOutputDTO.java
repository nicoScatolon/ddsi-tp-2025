package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgoritmoConcensoOutputDTO {
    private TipoAlgoritmoConsenso tipo;
}
