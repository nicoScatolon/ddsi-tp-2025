package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {
    List<HechoOutputDTO> buscarTodosLosHechos();
    HechoOutputDTO crearHecho(HechoInputDTO hecho);
    List<Hecho> obtenerTodosLasHechos(List<Fuente> fuentes);
    HechoOutputDTO buscarHechoPorId(Long id);

}
