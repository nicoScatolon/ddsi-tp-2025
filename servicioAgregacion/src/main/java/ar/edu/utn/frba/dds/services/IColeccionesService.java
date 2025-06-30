package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface IColeccionesService {
    ColeccionOutputDTO findByHandle(String handle);
    List<ColeccionOutputDTO> findAll();
    void crearColeccion(ColeccionInputDTO coleccionInputDTO);
    void modificarColeccion(ColeccionInputDTO coleccionInputDTO);
    void eliminarColeccion(ColeccionInputDTO coleccionInputDTO);
    HechosPaginadosResponseDTO mostrarHechosColeccion(String handle, int page, int size,List<CriterioInputDTO> criterios, Boolean curado);
    void notificarActualizacionFuentes(List<IFuente> fuentes);
    void notificarFuenteEliminada(IFuente fuente);
}