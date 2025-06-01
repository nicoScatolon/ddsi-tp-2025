package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<HechoBase> findAll();
    void actualizarHechosScheduler();
    HechoOutputDTO findByID(Long id);
    List<HechoBase> findByTipoFuente(List<TipoFuente> tiposFuentes);
    List<HechoBase> obtenerHechosProxy();
}