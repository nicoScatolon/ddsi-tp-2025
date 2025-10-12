package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.TipoAlgoritmoConsenso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgoritmoConsensoOutputDTO {
    private TipoAlgoritmoConsenso tipo;
}
