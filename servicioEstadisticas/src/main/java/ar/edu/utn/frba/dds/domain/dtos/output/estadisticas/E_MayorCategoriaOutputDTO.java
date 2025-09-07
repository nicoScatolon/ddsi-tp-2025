package ar.edu.utn.frba.dds.domain.dtos.output.estadisticas;

import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class E_MayorCategoriaOutputDTO {
    private Long id;
    private CategoriaOutputDTO categoriaDTO;
    private Integer cantHechosCategoria;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
