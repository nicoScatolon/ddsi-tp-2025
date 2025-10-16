package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionInputDTO {
    private Long id;

    private String provincia;
    private String departamento;
    private String calle;
    private Integer numero;

    private Double latitud;
    private Double longitud;
}
