package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor


@Entity
@Table(name = "hechos_externas")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "eliminado", nullable = false)
    private boolean eliminado = false;

    @Column(name = "id_original", nullable = false, unique = true)
    private String idOriginal;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Lob
    @Column(name = "descripcion", columnDefinition = "MEDIUMTEXT")
    private String descripcion;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Embedded
    private Ubicacion ubicacion;

    @Column (nullable = false, name = "fecha-ocurrencia")
    private LocalDateTime fechaDeOcurrencia;

    @Column (nullable = false, name = "fecha-carga")
    private LocalDateTime fechaDeCarga;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fuente_id", nullable = false)
    private Fuente fuente;


}
