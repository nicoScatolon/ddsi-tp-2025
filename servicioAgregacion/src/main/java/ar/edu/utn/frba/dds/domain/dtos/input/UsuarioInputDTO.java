package ar.edu.utn.frba.dds.domain.dtos.input;

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
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Boolean esAnonimo;
}
