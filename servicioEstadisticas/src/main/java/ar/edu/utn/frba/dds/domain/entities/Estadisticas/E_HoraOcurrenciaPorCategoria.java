package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class E_HoraOcurrenciaPorCategoria {
    private Long id;
    private Categoria categoria; //la condicion
    private Integer horaDia; //el resultado
    private Integer cantHechosHora;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}
