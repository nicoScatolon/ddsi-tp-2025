package ar.edu.utn.frba.dds.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Etiqueta {
    private Long id;
    private String nombre;

    public Etiqueta(String nombre){
        this.nombre = nombre;
    }

    //TODO ver si lo tratamos como una categoria (reutilizable) o como un simple string
    // Posiblemente le podemos agregar atributos para trazabilidad como el id del admin que lo agrego y el timestamp de cuando lo hizo
}
