package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.TipoFuenteProxy;

import java.util.List;

public interface IFuenteService {
    IFuente buscarPorId(Long id);
    List<IFuente> obtenerPorTipo(TipoFuenteProxy tipo);
    List<IFuente> obtenerTodas();
}
