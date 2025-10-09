package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {
    private final WebClient webClient;

    public FuenteDinamicaService(@Qualifier("dinamicaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResponseEntity<Void> crearHecho(HechoOutputDTO hechoOutputDTO) {
        return webClient.post()
                .uri("/api/fuenteDinamica/hechos")
                .contentType(MediaType.APPLICATION_JSON)            // asegurate de esto
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(hechoOutputDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
