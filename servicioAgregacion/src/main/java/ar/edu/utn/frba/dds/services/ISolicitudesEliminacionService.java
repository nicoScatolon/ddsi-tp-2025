package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;

import java.util.List;

public interface ISolicitudesEliminacionService {
    List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes();
    SolicitudEliminarHecho findByID(Long id);
}
