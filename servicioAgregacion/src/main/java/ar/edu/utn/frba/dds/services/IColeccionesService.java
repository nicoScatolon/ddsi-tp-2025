package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IColeccionesService {
    ColeccionOutputDTO findByHandle(String handle);
    List<ColeccionOutputDTO> findAll();
    ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO);
    void eliminarColeccion(ColeccionInputDTO coleccionInputDTO);
    List<HechoOutputDTO> mostrarHechosColeccion(String handle, Boolean curado, String categoria, LocalDateTime fReporteDesde, LocalDateTime fReporteHasta, LocalDate fAconDesde,LocalDate fAconHasta, Double latitud, Double longitud);
    void notificarActualizacionFuentes(List<IFuente> fuentes);
    void notificarFuenteEliminada(IFuente fuente);
    void modificarColeccionBasica(ColeccionInputDTO coleccionInputDTO);
    void modificarCriteriosColeccion (String handle, List<CriterioInputDTO> listaCriterioInputDTO);
    void modificarConsensoColeccion (String handle, AlgoritmoConsensoDTO consensoDTO);
    void modificarFuenteColeccion(String handle, List<FuenteInputDTO> fuenteInputDTO);
}