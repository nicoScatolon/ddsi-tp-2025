package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ServidoraDeHechosConFiltros extends ServidoraDeHechos{
    Mono<List<HechoExternoDTO>> buscarHechos(String categoria, LocalDateTime fechaReporteDesde, LocalDateTime fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate  fechaAcontecimientoHasta, Double latitud, Double longitud);
}
