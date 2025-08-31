package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeColecciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeEliminaciones;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeHechosConFiltros;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


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
    public Mono<List<HechoExternoDTO>> getHechos(){
        return webClient.get()
                .uri("/api/hechos/publica")
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();

    }



    @Override
    public Mono<List<HechoExternoDTO>> buscarHechos(HechosFilterDTO filtros) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/hechos/publica");
                    agregarFiltrosQueryParams(uriBuilder, filtros);
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
            String handle, Boolean curado, HechosFilterDTO filtros) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/publica/{handle}/hechos")
                            .queryParam("curado", curado);
                    agregarFiltrosQueryParams(uriBuilder, filtros);
                    return uriBuilder.build(handle);
                })
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .collectList();
    }




    @Override
    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoOutputDTO solicitud){
        return webClient.post()
                .uri("/api/solicitudes-eliminacion/publica")
                .bodyValue(solicitud)
                .retrieve()
                .bodyToMono(Void.class);
    }



    //Agrega los filtros del DTO a la query respetando camelCase
    private void agregarFiltrosQueryParams(org.springframework.web.util.UriBuilder uri, HechosFilterDTO f) {
        if (f == null) return;

        if (f.getCategoria() != null && !f.getCategoria().isEmpty())
            uri.queryParam("categoria", f.getCategoria());

        if (f.getFReporteDesde() != null)
            uri.queryParam("fechaReporteDesde", f.getFReporteDesde());

        if (f.getFReporteHasta() != null)
            uri.queryParam("fechaReporteHasta", f.getFReporteHasta());

        if (f.getFAconDesde() != null)
            uri.queryParam("fechaAcontecimientoDesde", f.getFAconDesde());

        if (f.getFAconHasta() != null)
            uri.queryParam("fechaAcontecimientoHasta", f.getFAconHasta());

        if (f.getLatitud() != null)
            uri.queryParam("latitud", f.getLatitud());

        if (f.getLongitud() != null)
            uri.queryParam("longitud", f.getLongitud());
    }



}