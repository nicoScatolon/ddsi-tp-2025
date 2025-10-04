package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaInputDTO {
    private String nombre;
    private String id;
}
