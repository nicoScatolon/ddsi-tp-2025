package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoMultimediaInputDTO {
    private Long Id;
    private String url;
    private String descripcion;
}
