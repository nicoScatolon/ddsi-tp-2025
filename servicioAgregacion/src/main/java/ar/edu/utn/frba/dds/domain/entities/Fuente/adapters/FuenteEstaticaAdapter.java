package ar.edu.utn.frba.dds.domain.entities.Fuente.adapters;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.FuenteEstatica;


import java.util.List;


public class FuenteEstaticaAdapter implements FuenteAdapter {
    private FuenteEstatica fuenteEstatica;

    @Override
    public List<IHechoInputDTO> obtenerHechosFuente() {
        return fuenteEstatica.getHechos().stream().map(dto -> (IHechoInputDTO) dto).toList();
    }
}
