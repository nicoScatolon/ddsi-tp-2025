package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioOutputDTO {
    String nombre;
    String apellido;
    String email;
    String currentPassword;
    String newPassword;
    String confirmNewPassword;
    LocalDate fechaNacimiento;
}
