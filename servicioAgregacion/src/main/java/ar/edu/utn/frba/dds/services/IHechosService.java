package ar.edu.utn.frba.dds.services;


import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IHechosService {
    List<Hecho> findAll();
    HechoOutputDTO findByID(Long id);
    Hecho findEntidadPorId(Long id);
    List<Hecho> findByFuente(Fuente fuente);

    void actualizarHechosRepository(List<Hecho> hechosActualizados);

    List<HechoOutputDTO> getHechos(HechosFilterDTO filterDTO, Boolean fueEliminado);
    List<HechoMapaOutputDTO> getHechosMapa();
    List<HechoMapaOutputDTO> getHechosMapaPorProvincia(String provincia);
    List<HechoOutputDTO> getHechosDestacados();

    ResponseEntity<Void> setDestacadoHecho(Long idHecho, Boolean estaDestacado);

    ResponseEntity<Void> agregarEtiquetasHecho(Long id, List<String> etiquetas);
    ResponseEntity<Void> guardarHecho(Hecho hecho);
    void eliminarHechos (List<Hecho> hechosAEliminar);
}