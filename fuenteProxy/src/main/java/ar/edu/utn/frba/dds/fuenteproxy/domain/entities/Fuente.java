package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import jakarta.persistence.*;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
@Table(name = "fuentes")
public abstract class Fuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    protected Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    protected String nombre;


    @Column(name = "baseURL", nullable = false)
    protected String baseUrl;

    @Column(name = "habilitada", nullable = false)
    protected boolean habilitada = true;



}
