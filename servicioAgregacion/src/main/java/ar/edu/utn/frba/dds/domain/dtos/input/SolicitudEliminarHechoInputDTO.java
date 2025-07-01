package ar.edu.utn.frba.dds.domain.dtos.input;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEliminarHechoInputDTO {
    private String razonDeEliminacion;
    private Long hechoId;
    private LocalDateTime fechaCreacion;
    private String nombreCreador;
    private String apellidoCreador;
}
