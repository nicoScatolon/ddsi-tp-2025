package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Usuario {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer edad;
    private Boolean esAnonimo;
    // si estan estos datos cargados significa que inicio sesion y por ende es un "contribuyente" anonimo o no
    // si estan vacios simplemente es un visualizador del sistema y no puede ni cargar hechos ni hacer solicitudes de eliminacion
    //TODO posible repositorio de usuarios para evitar repeticiones
    // no es necesario en realidad porque considero que directamente me mandan el id, no lo creo yo
}
