package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UbicacionInputDTO {
    private String provincia;
    private String departamento;
    private String calle;
    private Integer numero;
    private Double latitud;
    private Double longitud;
}
