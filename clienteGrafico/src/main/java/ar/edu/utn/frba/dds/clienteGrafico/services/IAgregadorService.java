package ar.edu.utn.frba.dds.clienteGrafico.services;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoMapaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechosFilterInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.HechosFilterOutputDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IAgregadorService {
    HechoInputDTO getHechoById(Long id);
    List<HechoInputDTO> getAllHechos(Integer paginaActual, HechosFilterInputDTO filter);
    List<HechoMapaInputDTO> getHechosMapa();
    List<ColeccionPreviewInputDTO> obtenerColeccionesPreview(Integer paginaActual);
    List<String> obtenerCategoriasShort();
    ResponseEntity<Void> crearSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion);
}
