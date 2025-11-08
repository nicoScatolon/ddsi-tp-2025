package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Usuarios.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioInputDTO {
    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    LocalDate fecha_nacimiento;
    LocalDate fecha_registro;
    LocalDateTime ultima_actividad;
    private Rol rol;
}
