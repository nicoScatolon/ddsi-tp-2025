package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAll();
    List<IHechoInputDTO> recolectarHechos(String fuenteURL);
    void actualizarHechosFuente(String fuenteURL);
    HechoOutputDTO findByID(Long id);
    void logearHechosCargados(List<IHecho> hechos, String urlFuente);
}