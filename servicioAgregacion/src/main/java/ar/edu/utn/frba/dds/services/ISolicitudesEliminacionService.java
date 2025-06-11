package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;

import java.util.List;

public interface ISolicitudesEliminacionService {
    List<SolicitudEliminarHechoOutputDTO> findAll();
    void rechazarSolicitud(SolicitudEliminarHechoInputDTO solicitud, UsuarioInputDTO usuarioInputDTO);
    void aceptarSolicitud(SolicitudEliminarHechoInputDTO solicitud, UsuarioInputDTO usuarioInputDTO);
    void crearSolicitud(HechoBase hecho, String razon, String nombre, String apellido);
    SolicitudEliminarHecho findByID(Long id);
    void logearSolicitudesEliminacionCargadas(List<SolicitudEliminarHecho> solicitudEliminarHechos);
}
