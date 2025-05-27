package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioInputDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Boolean esAnonimo;
}
