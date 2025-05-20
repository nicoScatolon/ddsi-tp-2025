package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.Getter;
import lombok.Setter;
import lombok.Data;

@Getter
@Setter
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
