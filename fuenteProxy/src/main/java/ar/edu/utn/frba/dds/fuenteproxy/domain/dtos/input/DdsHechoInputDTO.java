package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DdsHechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;


    private Double latitud;
    private Double longitud;

    @JsonProperty("fecha_hecho")
    private OffsetDateTime fechaHecho;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("updated_at")
    private OffsetDateTime updatedAt;
}
