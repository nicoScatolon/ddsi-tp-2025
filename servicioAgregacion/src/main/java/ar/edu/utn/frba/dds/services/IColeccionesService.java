package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionEditOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionPreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IColeccionesService {
    ColeccionOutputDTO findByHandle(String handle);
    List<ColeccionOutputDTO> findAll();
    List<ColeccionPreviewOutputDTO> findAllPreview(Integer page);
    ColeccionOutputDTO crearColeccion(ColeccionInputDTO coleccionInputDTO);
    ResponseEntity<Void> eliminarColeccion(String handle);
    List<HechoOutputDTO> mostrarHechosColeccion(String handle, Boolean curado, HechosFilterDTO hechosFilterDTO);
    void notificarActualizacionFuentes(List<Fuente> fuentes);
    void notificarFuenteEliminada(Fuente fuente);
    ColeccionOutputDTO modificarColeccionBasica(ColeccionInputDTO coleccionInputDTO);
    ResponseEntity<Void> modificarCriteriosColeccion (String handle, List<CriterioInputDTO> listaCriterioInputDTO);
    ResponseEntity<Void> modificarConsensoColeccion (String handle, AlgoritmoConsensoDTO consensoDTO);
    List<Fuente> modificarFuenteColeccion(String handle, List<FuenteInputDTO> fuenteInputDTO);

    ColeccionEditOutputDTO findByHandleEditable(String handle);

    ResponseEntity<Void> modificarColeccion(ColeccionInputDTO coleccionInputDTO);
}