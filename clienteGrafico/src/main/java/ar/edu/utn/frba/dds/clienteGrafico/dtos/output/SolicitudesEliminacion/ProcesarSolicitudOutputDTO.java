package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcesarSolicitudOutputDTO {
    private SolicitudEliminarHechoOutputDTO solicitud;
    private Long administradorId;
}
