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
@Table(name = "HoraOcurrenciaPorCategoria")
public class E_HoraOcurrenciaPorCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categoria")
    private String categoria; //la condicion del calculo

    @Column(name = "codigoCategoria")
    private String codigoCategoria; //la condicion del calculo

    @Column(name = "HoraDia")
    private Integer horaDia; //el resultado

    @Column(name = "cant-hechos-hora")
    private Integer cantHechosHora;

    @Column(name = "cant-hechos-totales")
    private Integer cantHechosTotales;

    @Column(name = "fecha-calculo", nullable = false)
    protected LocalDateTime fechaDeCalculo;
}
