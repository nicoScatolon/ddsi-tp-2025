package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAll();
    void actualizarHechosScheduler();
    void actualizarHechosManualmente();
    HechoOutputDTO findByID(Long id);
}