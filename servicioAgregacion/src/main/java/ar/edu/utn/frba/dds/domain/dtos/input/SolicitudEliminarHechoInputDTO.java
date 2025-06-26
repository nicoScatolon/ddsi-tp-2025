package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudEliminarHechoInputDTO {
    private String razonDeEliminacion;
    private Hecho hecho;
    private LocalDateTime fechaCreacion;
    private String nombreCreador;
    private String apellidoCreador;
}
