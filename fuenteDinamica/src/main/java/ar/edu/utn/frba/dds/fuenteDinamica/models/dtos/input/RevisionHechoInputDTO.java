package ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RevisionHechoInputDTO {
    Long idHecho;
    EstadoHecho nuevoEstado;
    String sugerencia;
}
