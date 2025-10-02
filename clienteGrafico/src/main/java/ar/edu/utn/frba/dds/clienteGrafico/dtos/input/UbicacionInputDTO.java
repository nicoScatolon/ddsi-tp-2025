package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.Data;

@Data
public class UbicacionInputDTO {
    private Long id;

    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;

    private Double latitud;
    private Double longitud;
}
