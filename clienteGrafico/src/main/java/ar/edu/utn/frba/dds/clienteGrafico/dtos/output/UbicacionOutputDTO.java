package ar.edu.utn.frba.dds.clienteGrafico.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UbicacionOutputDTO {
    private Long id;

    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;


    private Double latitud;
    private Double longitud;
}
