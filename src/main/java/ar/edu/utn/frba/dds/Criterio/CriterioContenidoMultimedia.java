package ar.edu.utn.frba.dds.Criterio;

import ar.edu.utn.frba.dds.Hechos.Hecho;

public class CriterioContenidoMultimedia implements Criterio {

    @Override
    public Boolean pertenece(Hecho hecho) {
        return false;
    }
}
