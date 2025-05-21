package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HechoService {
    private final WebClient webClient;

    @Value("${api.ddsi.base-url}")
    private String baseUrl;

    @Value("${api.ddsi.auth.email}")
    private String email;

    @Value("${api.ddsi.auth.password}")
    private String password;

    public HechoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    private Mono<String> autenticar(){
        String body = """
            {
                "email": "%s",
                "password": "%s"
            }
            """.formatted(email, password);

        return webClient
                .post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.get("token").asText());
    }

    private Mono<List<HechoDTO>> buscarTodos(){
        return autenticar()
                .flatMap(token -> webClient.get()
                .uri("/api/desastres?page=1")
                .retrieve()
                .bodyToFlux(HechoExternoDTO.class)
                .map(this::mapToHechoDTO)
                .collectList()
        );

    }

    public Mono<HechoDTO> buscarPorId(Long id) {
        return autenticar()
                .flatMap(token -> webClient.get()
                        .uri("/api/desastres/{id}", id)
                        .retrieve()
                        .bodyToMono(HechoExternoDTO.class)
                        .map(this::mapToHechoDTO)
        );
    }




    private HechoDTO mapToHechoDTO(HechoExternoDTO dto) {
    DateTimeFormatter fmt = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    return HechoDTO.builder()
            .id(dto.getId())
            .titulo(dto.getTitulo())
            .descripcion(dto.getDescripcion())
            .categoria(new CategoriaDTO(null, dto.getCategoria()))
            .ubicacion(new UbicacionDTO(dto.getLatitud(), dto.getLongitud()))
            .fechaDeOcurrencia(LocalDate.parse(dto.getFechaDeOcurrencia(), fmt))
            .fechaDeCarga(LocalDateTime.parse(dto.getFechaDeCarga(), fmt))
            .build();
}
}



