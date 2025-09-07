package ar.edu.utn.frba.dds.domain.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Coleccion {
    private String handle;
    private String titulo;
    private String descripcion;
    //el algoritmo de consenso no me interesa
    private List<Hecho> hechos;
}
