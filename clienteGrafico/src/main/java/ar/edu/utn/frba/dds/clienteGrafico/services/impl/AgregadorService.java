package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.DTOConverter;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.List;

@Service
public class AgregadorService implements IAgregadorService {

    private final WebClient webClient;

    public AgregadorService(@Qualifier("agregadorWebClient") WebClient webClient) {
        this.webClient = webClient;
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
}
