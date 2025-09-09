package ar.edu.utn.frba.dds.domain.entities.Categoria;

import jakarta.persistence.*;
import lombok.*;

import static ar.edu.utn.frba.dds.domain.entities.normalizadores.NormalizadorTexto.normalizarTexto;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "equivalente_categoria")
public class EquivalenteCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    Long id;

    @Column(name = "equivalente", nullable = false)
    String equivalente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    Categoria categoria;

    public EquivalenteCategoria(String nombreEquivalente, Categoria categoria) {
       this.equivalente = normalizarTexto(nombreEquivalente);
       this.categoria = categoria;
    }

}
