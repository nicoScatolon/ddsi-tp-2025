package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RevisionHechoInputDTO {
    Long idHecho;
    EstadoHecho nuevoEstado;
    String sugerencia;
}
