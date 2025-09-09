package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.PaginaHechosResponseDdsDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.LoginRequestDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeHechosPorId;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@DiscriminatorValue("EXTERNA")
public class FuenteDDS extends Fuente implements ServidoraDeHechosPorId {

    @Transient
    private TipoFuenteProxy tipo = TipoFuenteProxy.EXTERNA;

    @Transient
    private WebClient webClient;

    @Transient
    private String token;


    public FuenteDDS(String nombre,String baseUrl) {
        this.setNombre(nombre);
        this.setHabilitada(true);
        this.setBaseUrl(baseUrl);
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.token = this.login("ddsi@gmail.com", "ddsi2025*")
                .blockOptional()
                .orElseThrow(() -> new RuntimeException("No se pudo obtener el token de login"));
    }



    private Mono<String> login(String email, String password) {
        LoginRequestDTO request = new LoginRequestDTO(email, password);

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





    @Override
    @Transient
    public Mono<List<HechoInputDTO>> getHechos() {
        return webClient.get()
                .uri("/api/desastres?page=1")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(PaginaHechosResponseDdsDTO.class)
                .flatMap(primerPagina -> {
                    int lastPage = primerPagina.getLastPage();
                    List<HechoInputDTO> hechosTotales = new ArrayList<>(primerPagina.getData());

                    if (lastPage <= 1) {
                        return Mono.just(hechosTotales);
                    }

                    List<Mono<PaginaHechosResponseDdsDTO>> llamadasRestantes = new ArrayList<>();
                    for (int page = 2; page <= lastPage; page++) {
                        Mono<PaginaHechosResponseDdsDTO> llamada = webClient.get()
                                .uri("/api/desastres?page=" + page)
                                .headers(h -> h.setBearerAuth(token))
                                .retrieve()
                                .bodyToMono(PaginaHechosResponseDdsDTO.class);
                        llamadasRestantes.add(llamada);
                    }

                    return Mono.zip(llamadasRestantes, resultados -> {
                        for (Object resultado : resultados) {
                            PaginaHechosResponseDdsDTO pagina = (PaginaHechosResponseDdsDTO) resultado;
                            hechosTotales.addAll(pagina.getData());
                        }
                        return hechosTotales;
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
                .bodyToMono(HechoInputDTO.class);

    }

}
