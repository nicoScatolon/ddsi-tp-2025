package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Estadisticas;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MayorProvPorColeccionInputDTO {
    private Long id;
    private Integer cantHechosProvincia;
    private Integer cantHechosTotales;
    private String provincia;
    private String coleccion;       // o "coleccionHandle" / "handleColeccion", según tu DTO real
    private String handleColeccion; // opcional
    private LocalDateTime fechaCalculo;
}
