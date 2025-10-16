package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.ProcesarSolicitudInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ISolicitudesEliminacionService {
    List<SolicitudEliminarHechoOutputDTO> findAll(Long idCreador);
    List<SolicitudEliminarHechoOutputDTO> findSinProcesar();
    ResponseEntity<Void> crearSolicitudDesdeEntidad(Hecho hecho, String razon, Long idCreador);
    ResponseEntity<Void> crearSolicitudDesdeDTO(SolicitudEliminarHechoInputDTO solicitud);
    SolicitudEliminarHecho findByID(Long id);
    void logearSolicitudesEliminacionCargadas(List<SolicitudEliminarHecho> solicitudEliminarHechos);
    ResponseEntity<Void> procesarSolicitud(ProcesarSolicitudInputDTO solicitud, Boolean aceptar);
}
