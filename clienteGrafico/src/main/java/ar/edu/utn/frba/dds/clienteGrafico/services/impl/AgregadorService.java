package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
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

    public List<HechoOutputDTO> obtenerHechos(Integer paginaActual) {
        //TODO temporal para test
        List<HechoOutputDTO> hechos = new ArrayList<>();
        hechos.add(this.crearHecho1());
        hechos.add(this.crearHecho1());
        hechos.add(this.crearHecho1());
        return hechos;
    }

    public HechoOutputDTO getHechoById(Long id) {
        return webClient.get()
                .uri("/api/hecho/pubica/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new NotFoundException("hecho", id.toString())))
                .bodyToMono(HechoInputDTO.class)
                .map(DTOConverter::convertirHechoInputDTO)
                .block();
    }

    public List<HechoOutputDTO> getAllHechos() {
        return webClient.get()
                .uri("/api/hechos")
                .retrieve()
                .bodyToFlux(HechoInputDTO.class)
                .map(DTOConverter::convertirHechoInputDTO)
                .collectList()
                .block();
    }

    private HechoOutputDTO crearHecho1 (){
        HechoOutputDTO hecho = HechoOutputDTO.builder()
                .id(Long.valueOf(1))
                .titulo("titulo 1")
                .descripcion("descripcion 1")
                .categoria(CategoriaOutputDTO.builder()
                        .id("cat1")
                        .nombre("categoria 1")
                        .build())
                .ubicacion(UbicacionOutputDTO.builder()
                        .provincia("provincia 1")
                        .calle("calle 1")
                        .numero(124)
                        .build())
                .fechaDeCarga(LocalDateTime.now())
                .fechaDeOcurrencia(LocalDateTime.now())
                .cargadoAninimamente(true)
                .build();
        return hecho;
    }
}
