package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<Hecho> findAll();
    void actualizarHechosScheduler();
    HechoOutputDTO findByID(Long id);
    List<Hecho> findByFuente(List<IFuente> fuentes);
    List<Hecho> obtenerHechosProxy();
    Hecho findEntidadPorId(Long id);
    List<HechoOutputDTO>filtrarHechos(List<Hecho> hechos, List<ICriterio> criterios);
}