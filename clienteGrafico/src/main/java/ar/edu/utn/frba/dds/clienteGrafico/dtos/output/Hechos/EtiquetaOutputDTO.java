package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EtiquetaOutputDTO {
    private Long id;
    private String nombre;
}
