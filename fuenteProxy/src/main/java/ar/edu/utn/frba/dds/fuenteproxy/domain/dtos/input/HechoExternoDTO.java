package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import lombok.Data;

@Data
public class HechoExternoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String fechaDeOcurrencia;
    private String fechaDeCarga;



}
