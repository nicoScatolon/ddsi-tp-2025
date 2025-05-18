package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
}
