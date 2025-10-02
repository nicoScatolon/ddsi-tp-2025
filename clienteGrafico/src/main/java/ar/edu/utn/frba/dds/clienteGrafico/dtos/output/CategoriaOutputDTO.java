package ar.edu.utn.frba.dds.clienteGrafico.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriaOutputDTO {
    private String nombre;
    private String id;
}
