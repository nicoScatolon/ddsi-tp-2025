package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcesarSolicitudInputDTO {
    private SolicitudEliminarHechoInputDTO solicitud;
    private Long administradorId;
}
