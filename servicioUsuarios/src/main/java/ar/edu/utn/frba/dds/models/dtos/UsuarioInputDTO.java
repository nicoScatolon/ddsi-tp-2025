package ar.edu.utn.frba.dds.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioInputDTO {
    private String nombre;
    private String apellido;
    private String email;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
    private LocalDate fechaNacimiento;
}


