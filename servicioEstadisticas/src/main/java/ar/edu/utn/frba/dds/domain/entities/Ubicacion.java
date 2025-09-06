package ar.edu.utn.frba.dds.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Ubicacion {
    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;

    private Double latitud;
    private Double longitud;
}
