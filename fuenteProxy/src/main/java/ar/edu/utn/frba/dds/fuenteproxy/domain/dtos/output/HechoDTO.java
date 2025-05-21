package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoDTO {
    private Long           id;
    private String         titulo;
    private String         descripcion;
    private CategoriaDTO   categoria;
    private UbicacionDTO   ubicacion;
    private LocalDate      fechaDeOcurrencia;
    private LocalDateTime  fechaDeCarga;
}
