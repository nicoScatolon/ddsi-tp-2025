package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionOutputDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    UbicacionInputDTO ubicacion;
    private String fechaDeOcurrencia;
    private String fechaDeCarga;
}
