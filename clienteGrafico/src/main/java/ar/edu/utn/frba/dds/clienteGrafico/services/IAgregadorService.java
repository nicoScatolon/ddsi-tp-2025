package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.TipoAlgoritmoConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Colecciones.FiltroConsenso;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaEquivalenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.SolicitudesEliminacion.ProcesarSolicitudOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAgregadorService {
    HechoInputDTO getHechoById(Long id);
    List<HechoInputDTO> getAllHechos(Integer paginaActual, HechosFilterInputDTO filter);
    List<HechoMapaInputDTO> getHechosMapa();
    List<HechoMapaInputDTO> getHechosMapaPorProvincia(String provincia);

    List<ColeccionPreviewInputDTO> obtenerColeccionesPreview(Integer paginaActual, FiltroConsenso consenso);
    ColeccionPreviewInputDTO obtenerColeccionPreviewIndividual(String handle);
    List<HechoInputDTO> obtenerHechosColeccion(String handle, Integer paginaActual,  HechosFilterInputDTO filtros, Boolean curado);

    List<String> obtenerCategoriasShort();
    List<String> obtenerProvinciasShort();
    List<String> obtenerEtiquetasShort();

    List<CategoriaInputDTO> obtenerCategorias();
    List<EtiquetaInputDTO> obtenerEtiquetas();

    List<FuenteInputDTO> getFuentesPreview();

    ResponseEntity<Void> crearCategoria(CategoriaOutputDTO categoria);
    ResponseEntity<Void> editarCategoria(CategoriaOutputDTO categoria);

    ResponseEntity<Void> crearSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion);
    List<SolicitudEliminarHechoInputDTO>obtenerSolicitudesEliminacionPendientes();
    List<SolicitudEliminarHechoInputDTO> obtenerSolicitudesEliminacionUsuario(Long userId);
    ResponseEntity<Void> gestionarSolicitud(ProcesarSolicitudOutputDTO procesarSolicitudOutputDTO, EstadoDeSolicitud estadoDeSolicitud);

    ResponseEntity<Void> crearColeccion(ColeccionOutputDTO coleccionDTO);

    ResponseEntity<Void> eliminarColeccion(String handle);

    ColeccionInputDTO obtenerColeccion(String handle);

    ResponseEntity<Void> editarColeccion(ColeccionOutputDTO coleccionDTO);


    List<HechoInputDTO> obtenerHechosDestacados();
    ResponseEntity<Void> destacarHecho(Long id);
    ResponseEntity<Void> eliminarDestacarHecho(Long id);


    List<ColeccionPreviewInputDTO> obtenerColeccionesDestacadas();
    ResponseEntity<Void>destacarColeccion(String handle);
    ResponseEntity<Void> eliminarDestacarColeccion(String handle);

    List<CategoriaEquivalenteInputDTO> obtenerCatEquivalentes();
    ResponseEntity<Void> crearEquivalencia(CategoriaEquivalenteOutputDTO categoria);
    ResponseEntity<Void> eliminarEquivalencia(String categoria);

    ResponseEntity<Void> editarEquivalencia(CategoriaEquivalenteOutputDTO categoria);

    ResponseEntity<Void> modificarEtiquetas(Long id, List<String> etiquetas);

    ResponseEntity<Void> actualizarFuentesForzosamente();
    ResponseEntity<Void> actualizarColeccionesForzosamente();
    ResponseEntity<Void> curarColeccionesForzosamente();
}
