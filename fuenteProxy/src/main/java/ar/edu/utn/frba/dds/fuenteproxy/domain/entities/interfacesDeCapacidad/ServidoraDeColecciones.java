package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServidoraDeColecciones extends IFuente {
    Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones();
    Mono<List<HechoExternoDTO>> buscarHechosPorColeccion(String handle, String categoria, String fechaReporteDesde, String fechaReporteHasta,  String fechaAcontecimientoDesde,  String fechaAcontecimientoHasta,  String ubicacion, Boolean curado);
}
