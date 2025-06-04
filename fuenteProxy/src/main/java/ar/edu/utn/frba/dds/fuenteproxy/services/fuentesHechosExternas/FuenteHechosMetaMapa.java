package ar.edu.utn.frba.dds.fuenteproxy.services.fuentesHechosExternas;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteHechosExterna;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@Service
public class FuenteHechosMetaMapa implements IFuenteHechosExterna {
    private WebClient webClient;
    private final String baseUrl;

    public FuenteHechosMetaMapa(WebClient.Builder webClientBuilder, @Value("${api.metamapa.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public Mono<List<HechoExternoDTO>> buscarTodos() {
        return webClient.get()
                .uri("/api/hechos")
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();

    }


    public Mono<List<HechoExternoDTO>> buscarConFiltros(String categoria, String fechaReporteDesde, String fechaReporteHasta,  String fechaAcontecimientoDesde,  String fechaAcontecimientoHasta,  String ubicacion) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/hechos");
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




    public Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones(){
        return webClient.get()
                .uri("api/colecciones")
                .retrieve()
                .bodyToFlux(ColeccionInputDTO.class)
                .collectList();
    }


    public Mono<List<HechoExternoDTO>> buscarPorColeccion(String identificador) {
        return webClient.get()
                .uri("/colecciones/{id}/hechos", identificador)
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();
    }



    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud){
        webClient.post()
                .uri("/api/solicitudes")
                .bodyValue(solicitud)
                .retrieve()
                .bodyToMono(Void.class);

        return null;
    }


}