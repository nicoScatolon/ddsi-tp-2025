package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.*;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {

    private final WebClient webClient;

    public AgregadorService(@Qualifier("agregadorWebClient") WebClient webClient) {
        this.webClient = webClient;
    }


    public HechoInputDTO getHechoById(Long id) {
        return webClient.get()
                .uri("/api/hechos/publica/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new NotFoundException("hecho", id.toString())))
                .bodyToMono(HechoInputDTO.class)
                .block();
    }

    //TODO posiblemente debamos agregar la capacidad de recibir los filtros aca tambien
    public List<HechoInputDTO> getAllHechos(Integer paginaActual) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/hechos/publica")
                        .queryParam("page", paginaActual)
                        // .queryParam("categoria", "ejemplo") // para cuando necesitemos agregar filtros
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .collectList()
                .block();
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
                .descripcion("descripcion 1")
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
                .fechaDeOcurrencia(LocalDate.now())
                .cargadoAninimamente(true)
                .build();
    }
}
