package ar.edu.utn.frba.dds.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistroUsuarioDTO {
    String username;
    String password;
    String confirmPassword;
    String nombre;
    String apellido;
    String email;
    LocalDate fecha_nacimiento;
}
