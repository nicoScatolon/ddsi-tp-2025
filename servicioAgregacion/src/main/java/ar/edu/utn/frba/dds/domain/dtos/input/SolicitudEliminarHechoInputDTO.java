package ar.edu.utn.frba.dds.domain.dtos.input;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudEliminarHechoInputDTO {
    private String razonDeEliminacion;
    private Long hechoId;
    private LocalDateTime fechaCreacion;
    private String nombreCreador;
    private String apellidoCreador;
}
