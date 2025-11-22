package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Fuentes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuenteOutputDTO {
    private Long id;
    private String url;
    private String nombre;
    private TipoFuente tipoFuente;
}
