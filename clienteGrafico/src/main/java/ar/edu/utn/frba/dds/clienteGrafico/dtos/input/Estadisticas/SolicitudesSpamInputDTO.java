package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import java.time.LocalDateTime;

public class SolicitudesSpamInputDTO {
    private Long id;
    private Integer solicitudesSpam;
    private Integer solicitudesNoSpam;
    private LocalDateTime fechaDeCalculo;

    public SolicitudesSpamInputDTO(Object o, int i, int i1, Object o1) {
    }
}
