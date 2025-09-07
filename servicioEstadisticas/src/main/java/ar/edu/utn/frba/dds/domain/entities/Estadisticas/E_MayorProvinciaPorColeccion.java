package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "MayorProvinciaPorColeccion")
public class E_MayorProvinciaPorColeccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private static String descripcion = "La provincia con la mayor cantidad de hechos por cada coleccion";

    @Embedded
    private Coleccion coleccion; //la condicion del calculo

    @Column(name = "provincia")
    private String provincia; //el resultado

    @Column(name = "cant-hechos-provincia")
    private Integer cantHechosProvincia;

    @Column(name = "cant-hechos-totales")
    private Integer cantHechosTotales;

    @Column(name = "fecha-calculo")
    private LocalDateTime fechaDeCalculo;
}
