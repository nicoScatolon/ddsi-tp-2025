package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.IContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;

    private IContenidoMultimedia contenidoMultimedia; //todavia no se como es, va como null
}
