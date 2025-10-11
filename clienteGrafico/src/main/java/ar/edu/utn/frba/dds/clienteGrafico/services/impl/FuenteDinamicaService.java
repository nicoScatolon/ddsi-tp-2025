package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.util.List;

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

    @Override
    public List<HechoDinamicaInputDTO> obtenerHechosDinamica(EstadoHecho estadoHecho) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/fuenteDinamica/hechos")
                        .queryParam("estado", estadoHecho)
                        .build()
                )
                .retrieve()
                .bodyToFlux(HechoDinamicaInputDTO.class)
                .collectList()
                .block();
    }

    @Override
    public HechoDinamicaInputDTO obtenerHechoDinamicaId(Long idHecho) {
        return webClient.get()
                .uri("/api/fuenteDinamica/hechos/{id}", idHecho)
                .retrieve()
                .bodyToMono(HechoDinamicaInputDTO.class)
                .block();
        //TODO agregar metodo al backend
        // ver que onda si devuelve un 404
    }

    @Override
    public void enviarRevisionHechoDinamica(RevisionHechoInputDTO revisionHecho, Long adminId) {
        webClient.post()
                .uri("/api/fuenteDinamica/hechos/admin/{adminId}", adminId)
                .bodyValue(revisionHecho)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
