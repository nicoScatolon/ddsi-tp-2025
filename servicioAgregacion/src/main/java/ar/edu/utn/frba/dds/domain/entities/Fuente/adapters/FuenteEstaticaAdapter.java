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
    public List<Hecho> actualizarHechos() {
        return fuenteEstatica.updateHechos();
    }

    @Override
    public List<Hecho> obtenerHechos() {
        return fuenteEstatica.getMapHechos().values().stream().toList();
    }
}