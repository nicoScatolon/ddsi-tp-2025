package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionOutputDTO {
    private Long id;

    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;


    private Double latitud;
    private Double longitud;
}
