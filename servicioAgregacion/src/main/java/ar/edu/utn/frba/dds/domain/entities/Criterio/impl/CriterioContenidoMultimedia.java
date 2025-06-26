package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

public class CriterioContenidoMultimedia implements ICriterio {

    @Override
    public Boolean pertenece(Hecho hecho) {
        return false;
    }
}
