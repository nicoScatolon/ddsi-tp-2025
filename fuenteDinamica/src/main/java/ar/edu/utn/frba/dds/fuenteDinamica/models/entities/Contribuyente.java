package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Contribuyente {
    private Long id;
    // no tiene id -> no esta cargado en la base de datos de usuarios -> no esta registrado
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Boolean esAnonimo;
    // si estan estos datos cargados significa que inicio sesion y por ende es un "contribuyente" anonimo o no
}
