package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IFuenteMetaMapa extends IFuenteHechos {
    Mono<List<HechoExternoDTO>> buscarConFiltros(String categoria, String fechaReporteDesde, String fechaReporteHasta, String fechaAcontecimientoDesde, String fechaAcontecimientoHasta, String ubicacion);
    Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones();
    Mono<List<HechoExternoDTO>> buscarPorColeccion(String identificador);
    Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud);

}
