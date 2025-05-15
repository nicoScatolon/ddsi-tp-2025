package ar.edu.utn.frba.dds.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private long id;
    private String nombre;
    private String apellido;
}
