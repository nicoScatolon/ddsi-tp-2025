package ar.edu.utn.frba.dds.domain.entities;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor

@Embeddable
public class Ubicacion {
    private Double latitud;
    private Double longitud;

    public Ubicacion(Double latitud , Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }

    //Ver como compara sin que sea por exactamente la misma latitud o longitud, quiza con un rango de cercania o por paises
}
