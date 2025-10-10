package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAgregadorService {
    HechoInputDTO getHechoById(Long id);
    List<HechoInputDTO> getAllHechos(Integer paginaActual, HechosFilterInputDTO filter);
    List<HechoMapaInputDTO> getHechosMapa();

    List<ColeccionPreviewInputDTO> obtenerColeccionesPreview(Integer paginaActual);
    ColeccionPreviewInputDTO obtenerColeccionPreviewIndividual(String handle);
    List<HechoInputDTO> obtenerHechosColeccion(String handle, Integer paginaActual,  HechosFilterInputDTO filtros, Boolean curado);

    List<String> obtenerCategoriasShort();

    ResponseEntity<Void> crearSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion);

    List<FuenteInputDTO> getFuentesPreview();

    List<SolicitudEliminarHechoInputDTO>obtenerSolicitudesEliminacionPendientes();

    ResponseEntity<Void> gestionarSolicitud(ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO, EstadoDeSolicitud estadoDeSolicitud);
}
