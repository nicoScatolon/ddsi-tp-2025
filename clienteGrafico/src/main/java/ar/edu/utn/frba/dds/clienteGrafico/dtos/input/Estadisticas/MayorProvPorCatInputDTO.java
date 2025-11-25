package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.CategoriaInputDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MayorProvPorCatInputDTO {
    private Long id;
    private CategoriaInputDTO categoriaDTO; //la condicion del calculo
    private String provincia; //el resultado
    private Integer cantHechosProvincia;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
