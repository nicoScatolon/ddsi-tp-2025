package ar.edu.utn.frba.dds.domain.entities.Categoria;

import jakarta.persistence.*;
import lombok.*;

import static ar.edu.utn.frba.dds.domain.entities.Normalizadores.NormalizadorTexto.normalizarTexto;


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

    @Column(name = "nombreEquivalente", nullable = false)
    String nombreEquivalente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    Categoria categoria;

    public EquivalenteCategoria(String nombreEquivalente, Categoria categoria) {
       this.nombreEquivalente = normalizarTexto(nombreEquivalente);
       this.categoria = categoria;
    }

}
