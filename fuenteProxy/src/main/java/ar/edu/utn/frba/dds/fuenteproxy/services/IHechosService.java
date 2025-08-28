package ar.edu.utn.frba.dds.fuenteproxy.services;


import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {


    Mono<List<HechoOutputDTO>> buscarTodos();


    Mono<HechoOutputDTO> buscarPorId(Long id);


    Mono<List<HechoOutputDTO>> buscarConFiltros(HechosFilterDTO filtros);



}
