package ar.edu.utn.frba.dds.domain.dtos.output.estadisticas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class E_MayorCategoriaOutputDTO {
    private Long id;
    private String categoria; //la condicion
    private String codigoCategoria; //
    private Integer cantHechosCategoria;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
