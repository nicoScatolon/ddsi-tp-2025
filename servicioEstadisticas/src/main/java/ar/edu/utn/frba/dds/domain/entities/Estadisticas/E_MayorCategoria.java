package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class E_MayorCategoria {
    private Long id;
    private Categoria categoria;
    private Integer cantHechosCategoria;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
