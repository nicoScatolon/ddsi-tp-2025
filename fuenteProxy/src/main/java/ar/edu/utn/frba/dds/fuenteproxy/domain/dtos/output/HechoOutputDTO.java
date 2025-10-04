package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaOutputDTO categoria;
    private UbicacionOutputDTO ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
}
