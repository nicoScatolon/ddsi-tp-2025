package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoriaInputDTO;
    private UbicacionInputDTO ubicacionInputDTO;
    private LocalDate fechaDeOcurrencia;
    private Boolean esAnonimo;

    private List<ContenidoMultimedia> contenidoMultimedia;
    private ContribuyenteInputDTO contribuyenteInputDTO;
}
