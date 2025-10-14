package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudEliminacionInputDTO {
    private Long id;
    private HechoInputDTO hecho;
    private EstadoDeSolicitud estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaGestion;
    private Boolean eliminada;
}
