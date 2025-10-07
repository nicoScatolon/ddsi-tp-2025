package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UbicacionOutputDTO {
    private String provincia;
    private String departamento;
    private String calle;
    private Integer numero;
    private Double latitud;
    private Double longitud;
}
