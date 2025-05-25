package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ContribuyenteInputDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private Boolean esAnonimo;
}
