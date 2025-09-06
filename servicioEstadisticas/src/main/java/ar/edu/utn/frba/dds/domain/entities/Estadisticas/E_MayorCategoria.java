package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class E_MayorCategoria {
    private Long id;
    private Categoria categoria;
    private Integer cantHechosCategoria;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
