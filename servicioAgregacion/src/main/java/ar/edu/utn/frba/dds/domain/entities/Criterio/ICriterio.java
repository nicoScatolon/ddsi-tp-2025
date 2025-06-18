package ar.edu.utn.frba.dds.domain.entities.Criterio;


import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

public interface ICriterio {
    Boolean pertenece(Hecho hecho);
}
