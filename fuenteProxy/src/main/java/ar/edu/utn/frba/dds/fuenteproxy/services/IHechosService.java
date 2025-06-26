package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IHechosService {


    Mono<List<HechoOutputDTO>> buscarTodos();


    Mono<HechoOutputDTO> buscarPorId(Long id);


    Mono<List<HechoOutputDTO>> buscarConFiltros(String categoria,
                                                String fechaReporteDesde,
                                                String fechaReporteHasta,
                                                String fechaAcontecimientoDesde,
                                                String fechaAcontecimientoHasta,
                                                String ubicacion);


    Mono<List<ColeccionInputDTO>> traerTodasLasColecciones();


    Mono<List<HechoOutputDTO>> traerHechosDeColeccion(String identificador);


    Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud);


}
