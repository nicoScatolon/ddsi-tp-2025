package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoMapaOutputDTO {
    private Long id;
    private String titulo;
    private String categoria;
    private Double latitud;
    private Double longitud;
}
