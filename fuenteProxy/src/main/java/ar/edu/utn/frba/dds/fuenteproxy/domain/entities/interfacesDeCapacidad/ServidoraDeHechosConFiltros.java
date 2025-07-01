package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ServidoraDeHechosConFiltros extends ServidoraDeHechos{
    Mono<List<HechoExternoDTO>> buscarHechos(String categoria, String fechaReporteDesde, String fechaReporteHasta, String fechaAcontecimientoDesde, String fechaAcontecimientoHasta, String ubicacion);
}
