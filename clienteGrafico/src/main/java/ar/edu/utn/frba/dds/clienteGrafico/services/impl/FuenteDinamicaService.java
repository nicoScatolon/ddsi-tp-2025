package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.EstadoHecho;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoDinamicaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos.HechoDinamicaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IFuenteDinamicaService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class FuenteDinamicaService implements IFuenteDinamicaService {
    private final WebClient webClient;

    public FuenteDinamicaService(@Qualifier("dinamicaWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public ResponseEntity<Void> crearHecho(HechoDinamicaOutputDTO hechoDinamicaOutputDTO) {
        return webClient.post()
                .uri("/api/fuenteDinamica/hechos")
                .contentType(MediaType.APPLICATION_JSON)            // asegurate de esto
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(hechoDinamicaOutputDTO)
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
        try {
            return webClient.get()
                    .uri("/api/fuenteDinamica/hechos/{id}", idHecho)
                    .retrieve()
                    .bodyToMono(HechoDinamicaInputDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            return null;
        }
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

    @Override
    public List<HechoDinamicaInputDTO> obtenerHechosDinamicaUsuario(Long usuarioId, EstadoHecho estado, Integer page) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/fuenteDinamica/hechos/user/{userId}");
                    if (estado != null) uriBuilder.queryParam("estado", estado);
                    if (page != null) uriBuilder.queryParam("page", page);
                    return uriBuilder.build(usuarioId);
                })
                .retrieve()
                .bodyToFlux(HechoDinamicaInputDTO.class)
                .collectList()
                .block();
    }

    @Override
    public ResponseEntity<Void> editarHecho(HechoDinamicaOutputDTO hechoDTO) {
        return webClient.put()
                .uri("/api/fuenteDinamica/hechos/{id}", hechoDTO.getId())
                .bodyValue(hechoDTO)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
