package ar.edu.utn.frba.dds.services;


import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAllOutput();
    List<Hecho> findAll();
    HechoOutputDTO findByID(Long id);
    Hecho findEntidadPorId(Long id);
    List<Hecho> findByFuente(Fuente fuente);

    void actualizarHechosRepository(List<Hecho> hechosActualizados);

    List<HechoOutputDTO> getHechos(HechosFilterDTO filterDTO);
    List<HechoMapaOutputDTO> getHechosMapa();
    List<HechoOutputDTO> getHechosDestacados();

    ResponseEntity<Void> setDestacadoHecho(Long idHecho, Boolean estaDestacado);

    ResponseEntity<Void> agregarEtiquetaHecho(Long hechoId, String etiqueta);
    ResponseEntity<Void> eliminarEtiquetaHecho(Long hechoId, String etiqueta);
}