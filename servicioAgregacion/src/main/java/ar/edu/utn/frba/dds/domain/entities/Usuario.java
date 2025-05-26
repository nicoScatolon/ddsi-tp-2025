package ar.edu.utn.frba.dds.domain.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Usuario {
    private long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
}
