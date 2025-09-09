package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionOutputDTO {
    private String provincia;
    private String departamento;
    private String calle;
    private Integer numero;


    private Double latitud;
    private Double longitud;
}
