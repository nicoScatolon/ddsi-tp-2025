package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HechoExternoDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private Double latitud;
    private Double longitud;
    private String fechaDeOcurrencia;
    private String fechaDeCarga;
    private String updated_at;
}
