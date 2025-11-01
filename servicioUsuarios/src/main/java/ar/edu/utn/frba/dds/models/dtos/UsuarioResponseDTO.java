package ar.edu.utn.frba.dds.models.dtos;

import ar.edu.utn.frba.dds.models.entities.Rol;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String username;
    private String nombre;
    private String apellido;
    private String email;
    LocalDate fecha_nacimiento;
    LocalDate fecha_registro;
    LocalDateTime ultima_actividad;
    private Rol rol;
    private Boolean activo;      // idem
}
