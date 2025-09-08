package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class Categoria {
    @Column(name = "id_categoria")
    private String id;
    @Column(name = "nombre")
    private String nombre;
}
