package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Fuentes.TipoFuente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuenteInputDTO {
    private Long fuenteId;
    private String nombre;
    private TipoFuente tipoFuente;
}
