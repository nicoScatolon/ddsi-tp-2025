package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import lombok.Data;

@Data
public class FuenteInputDTO {
    private String url;
    private String nombre;
    private TipoFuente tipoFuente;
}
