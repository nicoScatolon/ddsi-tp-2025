package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Fuentes;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FuenteProxyOutputDTO {
    private String nombre;
    private String url;
    private String tipo;
}

