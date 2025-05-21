package ar.edu.utn.frba.dds.domain.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Ubicacion {
    private Double latitud;
    private Double longitud;

    public Ubicacion(Double latitud , Double longitud) {
        this.latitud = latitud;
        this.longitud = longitud;
    }
}
