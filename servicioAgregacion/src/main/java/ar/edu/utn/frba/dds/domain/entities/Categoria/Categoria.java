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
@Table(name = "categoria")
public class Categoria {

    @Id
    private String codigoCategoria; //es el nombre pero normalizado

    @Column(nullable = false)
    private String nombre;

}
