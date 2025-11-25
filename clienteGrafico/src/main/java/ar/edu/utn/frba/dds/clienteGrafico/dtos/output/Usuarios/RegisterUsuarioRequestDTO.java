package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios;

// DTO para ALTA / REGISTRO (request hacia el servicio de auth)

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RegisterUsuarioRequestDTO {
    String nombre;
    String apellido;
    String email;
    String username;
    String password;
    String confirmPassword;
    LocalDate fecha_nacimiento;
}
