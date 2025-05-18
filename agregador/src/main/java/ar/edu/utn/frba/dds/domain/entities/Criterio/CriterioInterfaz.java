package ar.edu.utn.frba.dds.domain.entities.Criterio;

import ar.edu.utn.frba.dds.domain.entities.Hecho;


public interface CriterioInterfaz {
    Boolean pertenece(Hecho hecho);
}
