package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

public class CriterioContenidoMultimedia implements CriterioInterfaz {

    @Override
    public Boolean pertenece(IHecho hecho) {
        return false;
    }
}
