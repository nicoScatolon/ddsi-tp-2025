package ar.edu.utn.frba.dds.fuenteproxy.services;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {


    Mono<List<HechoOutputDTO>> buscarTodos();


    Mono<HechoOutputDTO> buscarPorId(Long id, FuenteInputDTO fuente);


    Mono<List<HechoOutputDTO>> buscarConFiltros(HechosFilterDTO filtros);

    Mono<Void> cargarHechosFuente(Fuente fuente);

    void actualizarHechosScheduler();

    void eliminarHechosDeFuente(Long idFuente);
}
