package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Contribuyente;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoOutputDTO {
    private Long idLocal;
    private String titulo;
    private String descripcion;
    private CategoriaOutputDTO categoriaOutputDTO;
    private Ubicacion ubicacion;
    private LocalDate fechaOcurrencia;
    private LocalDateTime fechaCarga;
    private List<ContenidoMultimedia> contenidoMultimedia;
    private Contribuyente contribuyente;
    private Boolean esAnonimo;
}
