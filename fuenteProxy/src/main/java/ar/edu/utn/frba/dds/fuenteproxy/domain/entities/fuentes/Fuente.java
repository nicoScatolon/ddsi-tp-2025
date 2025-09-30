package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import jakarta.persistence.*;

import lombok.*;
import reactor.core.publisher.Mono;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "fuente")
@Table(name = "fuentes")
public abstract class Fuente implements IFuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;


    @Column(name = "baseURL", nullable = false, unique = true)
    private String baseUrl;

    @Column(name = "habilitada", nullable = false)
    private boolean habilitada = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoFuenteProxy tipo;

    public abstract Mono<List<HechoInputDTO>> getHechos();



}
