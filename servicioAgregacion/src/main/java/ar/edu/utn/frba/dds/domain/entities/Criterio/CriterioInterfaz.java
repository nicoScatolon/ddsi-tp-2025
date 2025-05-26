package ar.edu.utn.frba.dds.domain.entities.Criterio;


import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

public interface CriterioInterfaz {
    Boolean pertenece(IHecho hecho);
}
