package ar.edu.utn.frba.dds.Criterio;

import ar.edu.utn.frba.dds.Hechos.Hecho;


public interface Criterio {
    Boolean pertenece(Hecho hecho);
}
