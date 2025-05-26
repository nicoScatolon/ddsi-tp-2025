package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;

import java.util.List;

public interface ISolicitudesEliminacionService {
    List<SolicitudEliminarHechoOutputDTO> findAll();
    List<SolicitudEliminarHechoOutputDTO> recolectarSolicitudes(String fuenteURL);
    void crearSolicitud(IHecho hecho, String razon, String nombre, String apellido);
    SolicitudEliminarHecho findByID(Long id);
    void logearSolicitudesEliminacionCargadas(List<SolicitudEliminarHecho> solicitudEliminarHechos);
}
