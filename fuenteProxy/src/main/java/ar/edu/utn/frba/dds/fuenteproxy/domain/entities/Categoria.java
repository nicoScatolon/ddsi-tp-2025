package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Categoria {
    private Long id;
    private String nombre;

    public Categoria(Object o, String categoria) {
        this.nombre = categoria;
    }


}


