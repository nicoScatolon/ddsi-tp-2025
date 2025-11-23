package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "MayorCategoria")
public class E_MayorCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria; //la condicion del calculo

    @Column(name = "codigoCategoria")
    private String codigoCategoria; //la condicion del calculo

    @Column(name = "cant-hechos-categoria")
    private Integer cantHechosCategoria;

    @Column(name = "cant-hechos-totales")
    private Integer cantHechosTotales;

    @Column(name = "fecha-calculo", nullable = false)
    protected LocalDateTime fechaDeCalculo;
}
