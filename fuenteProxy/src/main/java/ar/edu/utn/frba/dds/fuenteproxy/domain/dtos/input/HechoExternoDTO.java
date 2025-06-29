package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
