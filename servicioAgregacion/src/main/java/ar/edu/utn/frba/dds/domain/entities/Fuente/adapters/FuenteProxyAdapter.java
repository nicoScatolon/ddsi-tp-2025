package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

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
    public List<Hecho> actualizarHechos(List<Hecho> hechosFuente) { return fuenteProxy.updateHechos(hechosFuente); }

    @Override
    public List<Hecho> obtenerHechos() { return fuenteProxy.getListaHechos(); }
}
