package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "etiqueta")
public class Etiqueta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;

    public Etiqueta(String nombre){
        this.nombre = nombre;
    }


    //TODO ver si lo tratamos como una categoria (reutilizable) o como un simple string
    // Posiblemente le podemos agregar atributos para trazabilidad como el id del admin que lo agrego y el timestamp de cuando lo hizo
}
