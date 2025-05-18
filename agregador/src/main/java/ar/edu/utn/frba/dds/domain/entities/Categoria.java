package ar.edu.utn.frba.dds.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Categoria {
    private String nombre;
    private Integer hash;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    //TODO ver como verificar si una categoria ya existe para no duplicarlas
}
