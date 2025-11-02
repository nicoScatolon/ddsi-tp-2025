package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.CategoriaInputDTO;

import java.time.LocalDateTime;

public class MayorProvPorCatInputDTO {
    private Long id;
    private CategoriaInputDTO categoriaDTO; //la condicion del calculo
    private String provincia; //el resultado
    private Integer cantHechosProvincia;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
