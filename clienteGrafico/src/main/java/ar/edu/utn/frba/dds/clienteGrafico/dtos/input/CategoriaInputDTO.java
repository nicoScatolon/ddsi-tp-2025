package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaInputDTO {
    private String nombre;
    private String id;
}
