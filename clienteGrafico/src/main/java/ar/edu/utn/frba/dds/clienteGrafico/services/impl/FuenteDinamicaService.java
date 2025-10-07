package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {
    private final WebClient webClient;

    public FuenteDinamicaService(@Qualifier("dinamicaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public ResponseEntity<Void> crearHecho(HechoInputDTO hechoInputDTO) {
        return webClient.post()
                .uri("/api/fuenteDinamica/hechos")
                .bodyValue(hechoInputDTO)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new NotFoundException("hecho", "N/A")))
                .toBodilessEntity()
                .block();
    }
}
