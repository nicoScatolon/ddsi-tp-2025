package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Ubicacion {
    //TODO agregar provincia y localidad
    private Double latitud;
    private Double longitud;
}
