package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeColecciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeEliminaciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeHechosConFiltros;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Data
public class FuenteMetaMapa implements ServidoraDeHechosConFiltros, ServidoraDeColecciones, ServidoraDeEliminaciones{
    private Long Id;
    private TipoFuenteProxy tipoFuenteProxy = TipoFuenteProxy.METAMAPA;
    private WebClient webClient;
    private final String baseUrl;

    public FuenteMetaMapa(@Value("${api.metamapa.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }



    @Override
    public Mono<List<HechoExternoDTO>> getHechos(){
        return webClient.get()
                .uri("/api/hechos/publica")
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();

    }



    @Override
    public Mono<List<HechoExternoDTO>> buscarHechos(String categoria, LocalDateTime fechaReporteDesde, LocalDateTime fechaReporteHasta,  LocalDate fechaAcontecimientoDesde,  LocalDate fechaAcontecimientoHasta,  Double latitud, Double  longitud) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/hechos/publica");
                    if (categoria != null && !categoria.isEmpty()) {
                        uriBuilder.queryParam("categoria", categoria);
                    }
                    if (fechaReporteDesde != null && !fechaReporteDesde.isEmpty()) {
                        uriBuilder.queryParam("fecha_reporte_desde", fechaReporteDesde);
                    }
                    if (fechaReporteHasta != null && !fechaReporteHasta.isEmpty()) {
                        uriBuilder.queryParam("fecha_reporte_hasta", fechaReporteHasta);
                    }
                    if (fechaAcontecimientoDesde != null && !fechaAcontecimientoDesde.isEmpty()) {
                        uriBuilder.queryParam("fecha_acontecimiento_desde", fechaAcontecimientoDesde);
                    }
                    if (fechaAcontecimientoHasta != null && !fechaAcontecimientoHasta.isEmpty()) {
                        uriBuilder.queryParam("fecha_acontecimiento_hasta", fechaAcontecimientoHasta);
                    }
                    if (latitud != 0) {
                        uriBuilder.queryParam("latitud", latitud);
                    }
                    if (longitud != 0) {
                        uriBuilder.queryParam("longitud", longitud);
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();
    }



    @Override
    public Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones(){
        return webClient.get()
                .uri("api/colecciones/publica/obtener-colecciones")
                .retrieve()
                .bodyToFlux(ColeccionInputDTO.class)
                .collectList();
    }


    @Override
    public Mono<List<HechoExternoDTO>> buscarHechosPorColeccion(
            String handle,
            String categoria,
            LocalDateTime fechaReporteDesde,
            LocalDateTime fechaReporteHasta,
            LocalDate fechaAcontecimientoDesde,
            LocalDate fechaAcontecimientoHasta,
            Double latitud,
            Double longitud,
            Boolean curado
    ) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder
                            .path("/api/publica/{handle}/hechos")
                            .queryParam("curado", curado)
                            .queryParamIfPresent("categoria", Optional.ofNullable(categoria).filter(s -> !s.isEmpty()))
                            .queryParamIfPresent("fecha_reporte_desde", Optional.ofNullable(fechaReporteDesde))
                            .queryParamIfPresent("fecha_reporte_hasta", Optional.ofNullable(fechaReporteHasta))
                            .queryParamIfPresent("fecha_acontecimiento_desde", Optional.ofNullable(fechaAcontecimientoDesde))
                            .queryParamIfPresent("fecha_acontecimiento_hasta", Optional.ofNullable(fechaAcontecimientoHasta))
                            .queryParamIfPresent("latitud", Optional.ofNullable(latitud))
                            .queryParamIfPresent("longitud", Optional.ofNullable(longitud));
                    return uriBuilder.build(handle);
                })
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();
    }






    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud){
        return webClient.post()
                .uri("api/solicitudes-eliminacion/publica/crear-solicitud-eliminacion")
                .bodyValue(solicitud)
                .retrieve()
                .bodyToMono(Void.class);
    }


}