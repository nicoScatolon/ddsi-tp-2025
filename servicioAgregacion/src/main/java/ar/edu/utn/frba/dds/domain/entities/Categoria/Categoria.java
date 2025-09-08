package ar.edu.utn.frba.dds.domain.entities.Categoria;

import jakarta.persistence.*;
import lombok.*;


import static ar.edu.utn.frba.dds.domain.normalizadores.NormalizadorTexto.normalizarTexto;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;


    public Categoria(String nombre) {

        this.nombre = nombre;
        new EquivalenteCategoria(normalizarTexto(this.nombre), this);
    }

}
