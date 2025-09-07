package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.Data;

@Data
public class UbicacionInputDTO {
    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;

    private Double latitud;
    private Double longitud;
}
