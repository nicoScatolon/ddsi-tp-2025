package ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> buscarTodosLosHechos();
    HechoOutputDTO crearHecho(HechoInputDTO hecho);
    List<Hecho> obtenerTodosLasHechos();
    HechoOutputDTO buscarHechoPorId(Long id);

}
