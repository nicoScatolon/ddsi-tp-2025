package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<Hecho> findAll();
    HechoOutputDTO findByID(Long id);
    Hecho findEntidadPorId(Long id);
    void actualizarHechosRepository(List<Hecho> hechosActualizados);
}