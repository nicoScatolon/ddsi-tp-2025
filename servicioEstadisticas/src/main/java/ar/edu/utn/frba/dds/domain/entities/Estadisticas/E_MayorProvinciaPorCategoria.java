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

public class E_MayorProvinciaPorCategoria {
    private Long id;
    //private static String descripcion = "La provincia con la mayor cantidad de hechos por cada categoria";
    private Categoria categoria; //la condicion del calculo
    private String provincia; //el resultado
    private Integer cantHechosProvincia;
    private Integer cantHechosTotales;
    private LocalDateTime fechaDeCalculo;
}

