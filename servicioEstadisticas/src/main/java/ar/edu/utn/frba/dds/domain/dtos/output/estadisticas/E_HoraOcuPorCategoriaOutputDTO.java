package ar.edu.utn.frba.dds.domain.dtos.output.estadisticas;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class E_HoraOcuPorCategoriaOutputDTO {
    //hora de ocurrencia mas comun por cada categoria
    private Long id;
    private String categoria; //la condicion
    private String codigoCategoria; //
    private Integer horaDia; //el resultado
    private Integer cantHechosHora;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
