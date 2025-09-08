package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Embeddable

public class Ubicacion {
    private Double latitud;
    private Double longitud;
} //Tiene sentido tener a la ubicación como entidad de dominio?

// Ubicación se modela como Value Object embebido dentro de Hecho.
// Justificación: una ubicación está definida únicamente por latitud/longitud.
// No tiene identidad propia ni se reutiliza entre hechos, por lo que no hace falta
// asignarle un ID ni tratarla como entidad de dominio.
// Si en el futuro necesitamos agrupar ubicaciones (ej. "CABA", "Provincia de BsAs"),
// lo vemos. KISS y YAGNI ;)