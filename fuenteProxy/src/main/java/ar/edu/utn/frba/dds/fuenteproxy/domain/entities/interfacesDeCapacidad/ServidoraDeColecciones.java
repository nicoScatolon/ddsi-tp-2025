package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.IFuente;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ServidoraDeColecciones extends IFuente {
    Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones();
    Mono<List<HechoExternoDTO>> buscarHechosPorColeccion(String handle, String categoria, LocalDateTime fechaReporteDesde, LocalDateTime fechaReporteHasta, LocalDate fechaAcontecimientoDesde, LocalDate fechaAcontecimientoHasta, Double latitud, Double longitud, Boolean curado);
}
