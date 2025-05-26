package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UbicacionOutputDTO {
    Double latitud;
    Double longitud;
}
