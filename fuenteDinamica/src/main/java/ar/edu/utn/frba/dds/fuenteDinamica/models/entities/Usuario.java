package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private Boolean esAnonimo;
    //TODO posible repositorio de usuarios para evitar repeticiones
}
