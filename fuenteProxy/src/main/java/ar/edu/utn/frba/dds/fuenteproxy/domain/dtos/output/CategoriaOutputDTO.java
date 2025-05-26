package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaOutputDTO {
    private Long   id;
    private String nombre;
}