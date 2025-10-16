package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

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
    String nombre;
    String apellido;
    String email;
    String password;
    LocalDate fechaNacimiento;
}
