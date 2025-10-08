package ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Builder
public class ContenidoMultimedia  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(255)", name = "url", nullable = false)
    private String url;

    @Column(columnDefinition = "VARCHAR(255)", name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private TipoContenido tipoContenido;

    @ManyToOne
    @JoinColumn(name = "hecho_id")   // FK a la tabla hechos
    private Hecho hecho;
}
