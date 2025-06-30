package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechosPaginadosDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.ServidoraDeColecciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.ServidoraDeEliminaciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.ServidoraDeHechosConFiltros;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
    public Mono<List<HechoExternoDTO>> buscarHechosConFiltros(String categoria, String fechaReporteDesde, String fechaReporteHasta,  String fechaAcontecimientoDesde,  String fechaAcontecimientoHasta,  String ubicacion) {

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
                    if (ubicacion != null && !ubicacion.isEmpty()) {
                        uriBuilder.queryParam("ubicacion", ubicacion);
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
    Mono<List<HechoExternoDTO>> buscarPorColeccion(
            String handle,
            List<ICriterio> criterios,
            boolean curado
    ) {

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