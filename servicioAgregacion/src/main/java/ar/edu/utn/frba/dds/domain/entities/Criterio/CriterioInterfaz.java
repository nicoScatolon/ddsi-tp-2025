package ar.edu.utn.frba.dds.domain.entities.Criterio;


import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;

public interface CriterioInterfaz {
    Boolean pertenece(HechoBase hecho);
}
