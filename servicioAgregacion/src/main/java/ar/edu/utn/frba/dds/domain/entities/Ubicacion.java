package ar.edu.utn.frba.dds.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "ubicacion")
public class Ubicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provincia")
    private String provincia;
    @Column(name = "localidad")
    private String localidad;
    @Column(name = "calle")
    private String calle;
    @Column(name = "numero")
    private Integer numero;

    @Column(name = "latitud")
    private Double latitud;
    @Column(name = "longitud")
    private Double longitud;

    //Ver como compara sin que sea por exactamente la misma latitud o longitud, quiza con un rango de cercania o por paises
}
