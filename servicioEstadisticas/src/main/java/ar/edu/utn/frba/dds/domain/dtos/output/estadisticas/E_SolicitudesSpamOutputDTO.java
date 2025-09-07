package ar.edu.utn.frba.dds.domain.dtos.output.estadisticas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class E_SolicitudesSpamOutputDTO {
    private Long id;
    private Integer solicitudesSpam;
    private Integer solicitudesNoSpam;
    private LocalDateTime fechaDeCalculo;
}
