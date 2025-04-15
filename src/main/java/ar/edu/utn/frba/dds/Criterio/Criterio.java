package ar.edu.utn.frba.dds.Criterio;

import ar.edu.utn.frba.dds.Hechos.Hecho;

public interface Criterio {
    public boolean pertenece(Hecho hecho);
}
