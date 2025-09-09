package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoriaOutputDTO {
    private String codigoCategoria;
    private String nombre;
}