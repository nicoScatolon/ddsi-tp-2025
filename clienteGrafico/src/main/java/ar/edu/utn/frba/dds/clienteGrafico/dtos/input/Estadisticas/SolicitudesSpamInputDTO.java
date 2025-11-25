package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SolicitudesSpamInputDTO {
    private Long id;
    private Integer solicitudesSpam;
    private Integer solicitudesNoSpam;
    private LocalDateTime fechaCalculo;
    }

