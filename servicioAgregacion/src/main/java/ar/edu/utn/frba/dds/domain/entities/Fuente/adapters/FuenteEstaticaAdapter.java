package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteDinamica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;


import java.util.List;


public class FuenteEstaticaAdapter implements FuenteAdapter {
    private FuenteEstatica fuenteEstatica;

    @Override
    public void setFuente(IFuente fuente) {
        if (! (fuente instanceof FuenteEstatica) ) {throw new RuntimeException("Fuente no valida");}
        else fuenteEstatica = (FuenteEstatica) fuente;
    }

    @Override
    public List<IHechoInputDTO> obtenerHechosFuente() {
        return fuenteEstatica.getHechos().stream().map(dto -> (IHechoInputDTO) dto).toList();
    }
}
