package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IColeccionesService {
    ColeccionOutputDTO findByHandle(String handle);
    List<ColeccionOutputDTO> findAll();
    ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO);
    List<HechoOutputDTO> hechosDeLaColeccion(String handle);
}