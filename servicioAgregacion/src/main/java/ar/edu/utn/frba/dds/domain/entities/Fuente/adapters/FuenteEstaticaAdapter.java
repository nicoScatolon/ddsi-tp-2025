package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;


import java.util.List;


public class FuenteEstaticaAdapter implements FuenteAdapter {
    private FuenteEstatica fuenteEstatica;

    @Override
    public void setFuente(IFuente fuente) {
        if (! (fuente instanceof FuenteEstatica) ) {throw new RuntimeException("Fuente no valida");}
        else fuenteEstatica = (FuenteEstatica) fuente;
    }

    @Override
    public List<Hecho> obtenerHechosFuente() {
        return fuenteEstatica.updateHechos();
    }

    @Override
    public List<Hecho> obtenerHechosCargados() {
        return fuenteEstatica.getMapHechos().values().stream().toList();
        //TODO si los tipos de fuentes consumidas/almacenadas puede modificarse por properties entonces esto tambien deberia
    }
}
