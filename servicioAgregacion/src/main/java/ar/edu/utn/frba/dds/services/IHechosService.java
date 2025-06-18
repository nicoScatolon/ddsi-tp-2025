package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<Hecho> findAll();
    void actualizarHechosScheduler();
    HechoOutputDTO findByID(Long id);
    List<Hecho> consumirHechos();

}