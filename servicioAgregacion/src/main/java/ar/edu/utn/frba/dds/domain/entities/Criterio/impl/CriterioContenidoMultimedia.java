package ar.edu.utn.frba.dds.domain.entities.Criterio.impl;


import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho;

public class CriterioContenidoMultimedia implements CriterioInterfaz {

    @Override
    public Boolean pertenece(Hecho hecho) {
        return false;
    }
}
