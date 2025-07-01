package ar.edu.utn.frba.dds.domain.dtos.output;

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
    private Long id;
    private String razonDeEliminacion;
    private HechoOutputDTO hecho;
    private LocalDateTime fechaCreacion;
    private String nombreCreador;
    private String apellidoCreador;
}