package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEquivalenteOutputDTO {
    String nombre;
    String codigoCategoria;
    String nuevoNombre;
}
