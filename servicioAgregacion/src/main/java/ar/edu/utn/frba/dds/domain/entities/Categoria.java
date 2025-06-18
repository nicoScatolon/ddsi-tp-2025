package ar.edu.utn.frba.dds.domain.entities;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {
    private String id;
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

}
