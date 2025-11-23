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
@Table(name = "MayorProvinciaPorCategoria")
public class E_MayorProvinciaPorCategoria {
    // "La provincia con la mayor cantidad de hechos por cada categoria";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria; //la condicion del calculo

    @Column(name = "codigoCategoria")
    private String codigoCategoria; //la condicion del calculo

    @Column(name = "provincia")
    private String provincia; //el resultado

    @Column(name = "cant-hechos-provincia")
    private Integer cantHechosProvincia;

    @Column(name = "cant-hechos-totales")
    private Integer cantHechosTotales;

    @Column(name = "fecha-calculo", nullable = false)
    protected LocalDateTime fechaDeCalculo;
}

