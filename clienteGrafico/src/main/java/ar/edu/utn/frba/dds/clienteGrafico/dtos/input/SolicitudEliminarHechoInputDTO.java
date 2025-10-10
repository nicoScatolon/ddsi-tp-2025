package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

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
    private Long id;
    private String razonDeEliminacion;
    private HechoInputDTO hecho;
    private LocalDateTime fechaCreacion;
    private Long idCreador;
}
