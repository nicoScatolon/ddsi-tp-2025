package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> findAll();
    List<HechoInputDTO> recolectarHechos(String fuenteURL);
    void actualizarHechosFuente(String fuenteURL); //ToDO revisar como el profe implementa la conexion entre servidores
    HechoOutputDTO buscarHechoPorId(Long id);
    void logearHechosCargados(List<Hecho> hechos, String urlFuente);
}
