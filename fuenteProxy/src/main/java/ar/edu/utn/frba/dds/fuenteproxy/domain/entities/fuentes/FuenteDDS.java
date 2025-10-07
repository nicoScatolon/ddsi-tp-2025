package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.DdsHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.PaginaHechosResponseDdsDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.LoginRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@DiscriminatorValue("DDS")
public class FuenteDDS extends Fuente {


    @Transient
    private WebClient webClient;

    @Transient
    private String token;


    public FuenteDDS(String nombre,String baseUrl) {
        this.setNombre(nombre);
        this.setHabilitada(true);
        this.setBaseUrl(baseUrl);
        this.iniciarFuente();

    }

    @PostLoad
    private void initAfterLoad() {
        this.iniciarFuente();
    }


    private void iniciarFuente(){
        this.setTipo(TipoFuenteProxy.EXTERNA);
        this.webClient = WebClient.builder().baseUrl(getBaseUrl()).build();
        this.token = this.login()
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("No se pudo obtener el token de login"));
    }



    private Mono<String> login() {
        LoginRequestDTO request = new LoginRequestDTO("ddsi@gmail.com", "ddsi2025*");

        return webClient.post()
                .uri("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(json -> {

                    if (json.path("error").asBoolean(false)) {
                        return Mono.error(new RuntimeException("Login fallido: " + json.path("message").asText()));
                    }
                    String accessToken = json.path("data").path("access_token").asText(null);
                    if (accessToken == null || accessToken.isBlank()) {
                        return Mono.error(new RuntimeException("No se recibió access_token en la respuesta"));
                    }
                    return Mono.just(accessToken);
                });
    }



    @Transient
    public Mono<List<HechoInputDTO>> getHechos() {
        return webClient.get()
                .uri(ub -> ub.path("/api/desastres").queryParam("page", 1).build())
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .onStatus(s -> s.value() == 401 || s.value() == 403,
                        r -> Mono.error(new RuntimeException("No autorizado al listar desastres (página 1)")))
                .bodyToMono(PaginaHechosResponseDdsDTO.class)
                .flatMap(p1 -> {
                    List<HechoInputDTO> acumulado = p1.getData().stream()
                            .map(DTOConverter::mapDdsToHechoInput)
                            .toList();

                    int last = p1.getLastPage();
                    if (last <= 1) return Mono.just(acumulado);

                    return Flux.range(2, last - 1)
                            .concatMap(page ->
                                    webClient.get()
                                            .uri(ub -> ub.path("/api/desastres").queryParam("page", page).build())
                                            .headers(h -> h.setBearerAuth(token))
                                            .retrieve()
                                            .onStatus(s -> s.value() == 401 || s.value() == 403,
                                                    r -> Mono.error(new RuntimeException("No autorizado al listar desastres (página " + page + ")")))
                                            .bodyToMono(PaginaHechosResponseDdsDTO.class)
                                            .map(resp -> resp.getData().stream()
                                                    .map(DTOConverter::mapDdsToHechoInput)
                                                    .toList())
                            )
                            .collectList()
                            .map(listas -> {
                                List<HechoInputDTO> todos = new ArrayList<>(acumulado);
                                listas.forEach(todos::addAll);
                                return todos;
                            });
                });
    }


    public Mono<HechoInputDTO> getHechoPorId(Long id) {
        return webClient.get()
                .uri("/api/desastres/{id}", id)
                .headers(h-> h.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response-> Mono.error(new RuntimeException("Desastre no encontrado con ID " + id)))
                .bodyToMono(DdsHechoInputDTO.class)
                .map(DTOConverter::mapDdsToHechoInput);

    }

}
