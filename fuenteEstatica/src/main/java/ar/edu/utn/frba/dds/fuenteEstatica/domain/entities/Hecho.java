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
    @Column (name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT", unique = true)
    private String descripcion;

    @Embedded
    private Ubicacion ubicacion;

    @Column(name = "categoria")
    private String categoria;

    @Column (name = "fechaDeOcurrencia")
    private LocalDate fechaDeOcurrencia;

    @Column (name = "archivoOrigen")
    private String archivoOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    //Metadata
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fechaDeCarga", nullable = false)
    private LocalDateTime fechaDeCarga;
}
