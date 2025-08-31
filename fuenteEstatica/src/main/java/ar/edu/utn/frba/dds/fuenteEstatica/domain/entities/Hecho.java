package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name= "hechos")
public class Hecho {
    //Datos
    @Column
    private String titulo;
    @Column(columnDefinition = "VARCHAR(2000)")
    private String descripcion;
    @Embedded
    private Ubicacion ubicacion;
    @Column
    private String categoria;
    @Column
    private LocalDate fechaDeOcurrencia;

    //Metadata
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fechaDeCarga;
}
