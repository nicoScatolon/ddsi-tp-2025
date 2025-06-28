package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;

import java.util.List;

public interface IColeccionesService {
    ColeccionOutputDTO findByHandle(String handle);
    List<ColeccionOutputDTO> findAll();
    //ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO);
    void crearColeccion(ColeccionInputDTO coleccionInputDTO);
    List<HechoOutputDTO> hechosDeLaColeccionByHandle(String handle);
    void actualizarColeccion(ColeccionInputDTO coleccionInputDTO);
    void modificarColeccion(ColeccionInputDTO coleccionInputDTO);
}