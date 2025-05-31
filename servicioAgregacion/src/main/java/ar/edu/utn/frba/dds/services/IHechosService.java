package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAll();
    void actualizarHechosScheduler();
    HechoOutputDTO findByID(Long id);
    List<HechoBase> obtenerHechosProxy();
}