package ar.edu.utn.frba.dds.clienteGrafico.dtos.output;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudEliminarHechoOutputDTO {
    private String razonDeEliminacion;
    private Long hechoId;
    private LocalDateTime fechaCreacion;
    private Long idCreador;
}
