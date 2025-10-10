package ar.edu.utn.frrba.dds.servicioUsuarios.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RegistroAdminDTO {
    String username;
    String password;
    String nombre;
    String apellido;
    String email;
}