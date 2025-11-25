package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.TipoContenido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoMultimediaInputDTO {
    private Long Id;
    private TipoContenido tipoContenido;
    private String url;
    private String descripcion;
}
