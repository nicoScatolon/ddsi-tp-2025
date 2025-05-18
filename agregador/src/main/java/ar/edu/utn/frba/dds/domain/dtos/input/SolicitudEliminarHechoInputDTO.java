package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.Hecho;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class SolicitudEliminarHechoInputDTO {
    private String razonDeEliminacion;
    private Hecho hecho;
    private LocalDate fechaCreacion;
    private String nombreCreador;
    private String apellidoCreador;
}
