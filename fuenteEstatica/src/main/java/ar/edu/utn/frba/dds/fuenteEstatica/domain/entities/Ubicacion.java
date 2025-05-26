package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ubicacion {
    private Double latitud;
    private Double longitud;
} //Tiene sentido tener a la ubicación como entidad de dominio?
