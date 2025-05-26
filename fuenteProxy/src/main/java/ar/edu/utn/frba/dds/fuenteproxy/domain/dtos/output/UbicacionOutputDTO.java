package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UbicacionOutputDTO {
    private Double latitud;
    private Double longitud;
}
