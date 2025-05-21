package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HechoInputDTO {
    private Long id;

    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoria;
    private UbicacionInputDTO ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeModificacion;
}