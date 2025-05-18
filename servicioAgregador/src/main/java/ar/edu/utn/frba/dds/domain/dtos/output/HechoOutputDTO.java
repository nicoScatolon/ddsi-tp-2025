package ar.edu.utn.frba.dds.domain.dtos.output;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
}

