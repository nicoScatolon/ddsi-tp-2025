package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@DiscriminatorValue("METAMAPA")
public class FuenteMetaMapa extends Fuente {


    @Transient
    private WebClient webClient;


    public FuenteMetaMapa(String nombre, String baseUrl) {
        this.setNombre(nombre);
        this.setHabilitada(true);
        this.setBaseUrl(baseUrl);
        this.iniciarFuente();
    }

    @PostLoad
    private void initAfterLoad() {
        this.iniciarFuente();
    }


    private void iniciarFuente() {
        this.setTipo(TipoFuenteProxy.METAMAPA);
        this.webClient = WebClient.builder().baseUrl(getBaseUrl()).build();
    }




    @Transient
    public Mono<List<HechoInputDTO>> getHechos(){
        return webClient.get()
                .uri("/api/hechos/publica")
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList();

    }




    public Mono<List<HechoInputDTO>> buscarHechos(HechosFilterDTO filtros) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/hechos/publica");
                    agregarFiltrosQueryParams(uriBuilder, filtros);
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList();
    }






    public Mono<List<ColeccionInputDTO>> buscarTodasLasColecciones(){
        return webClient.get()
                .uri("api/colecciones/publica/obtener-colecciones")
                .retrieve()
                .bodyToFlux(ColeccionInputDTO.class)
                .collectList();
    }



    public Mono<List<HechoInputDTO>> buscarHechosPorColeccion(
            String handle, Boolean curado, HechosFilterDTO filtros) {

        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/publica/{handle}/hechos")
                            .queryParam("curado", curado);
                    agregarFiltrosQueryParams(uriBuilder, filtros);
                    return uriBuilder.build(handle);
                })
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList();
    }





    public Mono<Void> crearSolicitudEliminacion(SolicitudEliminarHechoInputDTO solicitud){
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