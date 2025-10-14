package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class Ubicacion {

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "departamento")
    private String departamento;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;
}
