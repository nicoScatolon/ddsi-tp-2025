package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolicitudEliminacionInputDTO {
    private Long idLocalHecho;
    private String razonDeEliminacion;
    private LocalDateTime fechaCreacion;
    private ContribuyenteInputDTO contribuyenteInputDTO;
}
