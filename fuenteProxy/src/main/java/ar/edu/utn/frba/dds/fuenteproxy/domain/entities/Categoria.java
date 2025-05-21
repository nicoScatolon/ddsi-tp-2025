package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Categoria {
    private Long id;
    private String nombre;

    public Categoria(String categoria) {
        this.nombre = categoria;
    }


}


