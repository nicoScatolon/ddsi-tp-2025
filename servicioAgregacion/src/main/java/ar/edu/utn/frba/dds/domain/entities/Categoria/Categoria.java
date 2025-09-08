package ar.edu.utn.frba.dds.domain.entities.Categoria;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EquivalenteCategoria> equivalentes = new HashSet<>();

    public Categoria(String nombre) {

        this.nombre = nombre;
        new EquivalenteCategoria(normalizarTexto(this.nombre), this);
    }



    public void addEquivalente(EquivalenteCategoria eq) {
        equivalentes.add(eq);
        eq.setCategoria(this);
    }

    public void removeEquivalente(EquivalenteCategoria eq) {
        equivalentes.remove(eq);
        eq.setCategoria(null);
    }

}
