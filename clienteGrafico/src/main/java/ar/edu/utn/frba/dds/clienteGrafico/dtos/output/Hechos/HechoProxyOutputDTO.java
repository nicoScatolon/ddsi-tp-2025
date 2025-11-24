package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos;

import java.time.LocalDateTime;

public class HechoProxyOutputDTO {
    private Long           id;
    private String         titulo;
    private String         descripcion;
    private CategoriaOutputDTO categoria;
    private UbicacionOutputDTO ubicacion;
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime  fechaDeCarga;
}
