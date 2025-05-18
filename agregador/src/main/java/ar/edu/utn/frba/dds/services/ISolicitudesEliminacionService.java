package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;

import java.util.List;

public interface ISolicitudesEliminacionService {
    List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes();
}
