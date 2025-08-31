package ar.edu.utn.frba.dds.domain.entities.Fuente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
@Table(name = "fuentes")
public abstract class Fuente implements IFuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 120)
    protected String nombre;

    @Column(nullable = false)
    protected String url;
}
