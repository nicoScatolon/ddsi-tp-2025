package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoMapaInputDTO {
    private Long id;
    private String titulo;
    private String categoria;
    private Double latitud;
    private Double longitud;
}
