package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.adapters.IFuenteAdapter;

import java.util.List;

public interface IFuenteService {
    IFuenteAdapter buscarPorId(Long id);
    List<IFuenteAdapter> obtenerPorTipo(TipoFuenteProxy tipo);
    List<IFuenteAdapter> obtenerTodas();
}
