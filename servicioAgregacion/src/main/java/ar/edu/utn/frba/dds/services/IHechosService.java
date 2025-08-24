package ar.edu.utn.frba.dds.services;


import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<Hecho> findAll();
    HechoOutputDTO findByID(Long id);
    Hecho findEntidadPorId(Long id);
    void actualizarHechosRepository(List<Hecho> hechosActualizados);
    List<HechoOutputDTO> getHechos(HechosFilterDTO filterDTO);
    ResponseEntity<Void> agregarEtiquetaHecho(Long hechoId, String etiqueta);
    ResponseEntity<Void> eliminarEtiquetaHecho(Long hechoId, String etiqueta);
}