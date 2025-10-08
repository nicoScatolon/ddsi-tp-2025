package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia.TipoContenido;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContenidoMultimediaInputDTO {
    private Long id;
    private String url;
    private String descripcion;
    private TipoContenido tipo;
}
