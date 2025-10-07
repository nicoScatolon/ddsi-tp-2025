package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "Fuente")
public class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private FormatoFuente tipo;

    @Column(nullable=false, length=1024)
    private String uri;

    @Column(nullable=false, length=32)
    private String estrategia;

    @OneToMany(mappedBy = "fuente")
    private List<Hecho> hechos;
}
