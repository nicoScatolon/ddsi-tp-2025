package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.CategoriaInputDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MayorCategoriaInputDTO {
    private Long id;
    private Integer cantHechosCategoria;
    private Integer cantHechosTotales;
    private String categoria;
    private String codigoCategoria;
    private LocalDateTime fechaCalculo;
}
