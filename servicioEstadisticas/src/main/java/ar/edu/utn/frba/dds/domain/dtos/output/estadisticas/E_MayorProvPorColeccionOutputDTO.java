package ar.edu.utn.frba.dds.domain.dtos.output.estadisticas;

import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class E_MayorProvPorColeccionOutputDTO {
    private Long id;
    private ColeccionOutputDTO coleccionDTO; //la condicion del calculo
    private String provincia; //el resultado
    private Integer cantHechosProvincia;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
