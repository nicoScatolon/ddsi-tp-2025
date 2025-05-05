package ar.edu.utn.frba.dds.Hechos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Usuario {
    private String nombre;
    private String apellido;
    private Enum tipoUsuario;
}
