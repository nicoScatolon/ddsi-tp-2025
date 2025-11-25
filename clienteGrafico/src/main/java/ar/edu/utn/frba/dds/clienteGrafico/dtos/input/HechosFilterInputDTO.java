package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechosFilterInputDTO {
    //el que recibimos desde el front
    private String categoria;

    private LocalDate fReporteDesde;

    private LocalDate fReporteHasta;

    private LocalDate fAconDesde;

    private LocalDate fAconHasta;

    private String provincia;

    private Long fuenteId;

    private String etiqueta; //Todo Podriamos hacer q se puedan seleccioanr varias etiquetas, porq un hecho puede tener mas de una etiqueta
}
