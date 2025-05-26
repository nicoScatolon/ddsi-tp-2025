package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.IContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoOutputDTO {
    private Long idLocal;
    private String titulo;
    private String descripcion;
    private String nombreCategoria;
    private Ubicacion ubicacion;
    private LocalDate fechaOcurrencia;
    private LocalDateTime fechaCarga;
    private IContenidoMultimedia contenidoMultimedia;
    private Contribuyente contribuyente;
}
