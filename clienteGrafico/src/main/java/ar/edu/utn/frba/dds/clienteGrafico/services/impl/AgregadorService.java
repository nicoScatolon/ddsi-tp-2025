package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.*;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.*;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {

    private final WebClient webClient;

    public AgregadorService(@Qualifier("agregadorWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    // --- HECHOS --- //

    public HechoInputDTO getHechoById(Long id) {
        return webClient.get()
                .uri("/api/hechos/publica/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new NotFoundException("hecho", id.toString())))
                .bodyToMono(HechoInputDTO.class)
                .block();
    }

    public List<HechoInputDTO> getAllHechos(Integer paginaActual, HechosFilterInputDTO filterInputDTO) {
        HechosFilterOutputDTO filter = DTOConverter.convertirHechosFilterInputDTO(filterInputDTO);

        return webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/api/hechos/publica")
                            .queryParam("page", paginaActual);
                    aplicarFiltrosHecho(builder, filter);
                    return builder.build();
                })
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList()
                .block();
    }

    public List<HechoMapaInputDTO> getHechosMapa(){
        return webClient.get()
                .uri("/api/hechos/publica/mapa")
                .retrieve()
                .bodyToFlux(HechoMapaInputDTO.class)
                .collectList()
                .block();
    }

    // --- COLECCIONES --- //

    public List<ColeccionPreviewInputDTO> obtenerColeccionesPreview(Integer paginaActual) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/colecciones/publica/preview")
                        .queryParam("page", paginaActual)
                        // .queryParam("categoria", "ejemplo") // para cuando necesitemos agregar filtros
                        .build()
                )
                .retrieve()
                .bodyToFlux(ColeccionPreviewInputDTO.class)
                .collectList()
                .block();
    }

    // --- CATEGORIAS --- //

    public List<String> obtenerCategoriasShort(){
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/privada/categorias/short")
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();
    }

    // --- SOLICITUDES ELIMINACION --- //
    @Override
    public ResponseEntity<Void> crearSolicitudEliminacion(Long hechoId, Long usuarioId, String razonEliminacion){
        SolicitudEliminarHechoOutputDTO request = DTOConverter.convertirSolicitudEliminacion(hechoId, usuarioId, razonEliminacion);
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/solicitudes-eliminacion/publica")
                        .build()
                )
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // --- METODOS PRIVADOS --- //

    private void aplicarFiltrosHecho(UriBuilder builder, HechosFilterOutputDTO filter) {
        if (filter.getCategoria() != null && !filter.getCategoria().isEmpty()) {
            builder.queryParam("categoria", filter.getCategoria());
        }
        if (filter.getProvincia() != null && !filter.getProvincia().isEmpty()) {
            builder.queryParam("provincia", filter.getProvincia());
        }
        if (filter.getEtiqueta() != null && !filter.getEtiqueta().isEmpty()) {
            builder.queryParam("etiqueta", filter.getEtiqueta());
        }
        if (filter.getFuenteId() != null) {
            builder.queryParam("fuenteId", filter.getFuenteId());
        }

        if (filter.getFReporteDesde() != null) {
            builder.queryParam("fReporteDesde", filter.getFReporteDesde());
        }
        if (filter.getFReporteHasta() != null) {
            builder.queryParam("fReporteHasta", filter.getFReporteHasta());
        }
        if (filter.getFAconDesde() != null) {
            builder.queryParam("fAconDesde", filter.getFAconDesde());
        }
        if (filter.getFAconHasta() != null) {
            builder.queryParam("fAconHasta", filter.getFAconHasta());
        }
    }



    // --- TEST --- //
    public List<HechoInputDTO> obtenerHechos(Integer paginaActual) {
        //TODO temporal para test
        List<HechoInputDTO> hechos = new ArrayList<>();
        hechos.add(this.crearHecho1());
        hechos.add(this.crearHecho1());
        hechos.add(this.crearHecho1());
        return hechos;
    }

    private HechoInputDTO crearHecho1 (){
        return HechoInputDTO.builder()
                .id(Long.valueOf(1))
                .titulo("titulo 1")
                .descripcion("Lorem  vel nobis")
                .categoria(CategoriaInputDTO.builder()
                        .id("cat1")
                        .nombre("categoria 1")
                        .build())
                .ubicacion(UbicacionInputDTO.builder()
                        .provincia("Buenos Aires")
                        .localidad("CABA")                // Ciudad Autónoma de Buenos Aires
                        .calle("Av. Corrientes")          // Calle conocida
                        .numero(1234)
                        .latitud(-34.6037)                // Coordenadas aproximadas
                        .longitud(-58.3816)
                        .build())
                .fechaDeCarga(LocalDateTime.now())
                .fechaDeOcurrencia(LocalDateTime.now())
                .cargadoAnonimamente(true)
                .build();
    }
}
