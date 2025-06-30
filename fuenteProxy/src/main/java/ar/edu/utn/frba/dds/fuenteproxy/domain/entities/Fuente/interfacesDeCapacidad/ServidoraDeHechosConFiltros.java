package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.IFuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServidoraDeHechosConFiltros extends IFuente {
    Mono<List<HechoExternoDTO>> buscarHechosConFiltros(String categoria, String fechaReporteDesde, String fechaReporteHasta, String fechaAcontecimientoDesde, String fechaAcontecimientoHasta, String ubicacion);
}
