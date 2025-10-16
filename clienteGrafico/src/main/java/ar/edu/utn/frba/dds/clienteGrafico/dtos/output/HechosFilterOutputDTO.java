package ar.edu.utn.frba.dds.clienteGrafico.dtos.output;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechosFilterOutputDTO {
    // el que le mandamos al back
    private String categoria;

    private LocalDateTime fReporteDesde;

    private LocalDateTime fReporteHasta;

    private LocalDateTime fAconDesde;

    private LocalDateTime fAconHasta;

    private String provincia;

    private Long fuenteId;

    private String etiqueta;
}
