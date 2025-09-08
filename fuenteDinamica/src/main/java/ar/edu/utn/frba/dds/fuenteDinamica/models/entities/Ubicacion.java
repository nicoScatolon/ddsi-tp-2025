package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
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

    @Column(name = "latitud", nullable = false)
    private Double latitud;
    @Column(name = "longitud", nullable = false)
    private Double longitud;
}
