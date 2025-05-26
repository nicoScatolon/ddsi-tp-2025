package ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Ubicacion;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder

public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
}
