package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteProxy;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public class FuenteProxyAdapter implements FuenteAdapter {
    private FuenteProxy fuenteProxy;

    @Override
    public void setFuente(IFuente fuente) {
        if (! (fuente instanceof FuenteProxy) ) {throw new RuntimeException("Fuente no valida");}
        else fuenteProxy = (FuenteProxy) fuente;
    }

    @Override
    public List<Hecho> obtenerHechosFuente() {
        return fuenteProxy.getHechos();
    }

    @Override
    public List<Hecho> obtenerHechosCargados() {
        return null;
    }
}
