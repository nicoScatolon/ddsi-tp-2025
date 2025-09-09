package ar.edu.utn.frba.dds.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Embeddable
public class Coleccion {
    @Column(name = "coleccion_handle")
    private String handle;
    @Column(name = "coleccion_titulo")
    private String titulo;
    @Transient
    private String descripcion;
    //el algoritmo de consenso no me interesa
    @Transient
    private List<Hecho> hechos;
}
