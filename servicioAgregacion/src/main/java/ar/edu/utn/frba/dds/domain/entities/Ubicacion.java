package ar.edu.utn.frba.dds.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Ubicacion {
    private Long id;

    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;


    private Double latitud;
    private Double longitud;

}
