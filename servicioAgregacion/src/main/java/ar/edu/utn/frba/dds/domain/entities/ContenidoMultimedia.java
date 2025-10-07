package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo")
public abstract class ContenidoMultimedia implements IContenidoMultimedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(columnDefinition = "VARCHAR(255)", name = "url", nullable = false)
    private String url;

    @Column(columnDefinition = "VARCHAR(255)", name = "descipcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "hecho_id")   // FK a la tabla hechos
    private Hecho hecho;
}
