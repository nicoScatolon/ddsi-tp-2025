package ar.edu.utn.frba.dds.fuenteproxy.services.fuentesHechosExternas;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.PaginaHechosResponseDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.AuthRequestDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteHechosExterna;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class FuenteHechosDds implements IFuenteHechosExterna {

    private final WebClient webClient;

    private final Mono<String> token;

    public FuenteHechosDds(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
        this.token = this.autenticar().cache();
    }

    @Value("${api.ddsi.base-url}")
    private String baseUrl;

    @Value("${api.ddsi.auth.email}")
    private String email;

    @Value("${api.ddsi.auth.password}")
    private String password;

    private Mono<String> autenticar(){
        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .email(email)
                .password(password)
                .build();

        return webClient
                .post()
                .uri("/api/login")
                .bodyValue(authRequestDTO)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    boolean hayError = json.get("hayError").asBoolean();
                    if (hayError) {
                        throw new RuntimeException("Error en login: " + json.get("message").asText());
                    }
                    return json.get("token").asText();
                });

    }


    public Mono<List<HechoExternoDTO>> buscarTodos() {
        return token.flatMap(token ->
                webClient.get()
                        .uri("/api/desastres?page=1")
                        .headers(h -> h.setBearerAuth(token))
                        .retrieve()
                        .onStatus(status -> status.value() == 401,
                                response  -> Mono.error(new RuntimeException("No autenticado: el token es inválido")))
                        .bodyToMono(PaginaHechosResponseDTO.class)
                        .flatMap(primerPagina -> {
                            int lastPage = primerPagina.getLastPage();
                            List<HechoExternoDTO> hechosTotales = new ArrayList<>(primerPagina.getData());

                            List<Mono<PaginaHechosResponseDTO>> llamadasRestantes = new ArrayList<>();
                            for(int page =2; page <= lastPage; page++) {
                                Mono<PaginaHechosResponseDTO> llamada = webClient.get()
                                        .uri("/api/desastres?page=" + page)
                                        .headers(h->h.setBearerAuth(token))
                                        .retrieve()
                                        .bodyToMono(PaginaHechosResponseDTO.class);
                                llamadasRestantes.add(llamada);

                            }

                            return Mono.zip(llamadasRestantes,resultados->{
                                for(Object resultado : resultados) {
                                    PaginaHechosResponseDTO pagina = (PaginaHechosResponseDTO) resultado;
                                    hechosTotales.addAll(pagina.getData());
                                }
                                return hechosTotales;
                            });


                        })
        );

    }



    public Mono<HechoExternoDTO> buscarPorId(Long id) {
        return token.flatMap(token ->
                webClient.get()
                .uri("/api/desastres/{id}", id)
                .headers(h-> h.setBearerAuth(token))
                        .retrieve()
                        .onStatus(status -> status.value() == 401,
                                response -> Mono.error(new RuntimeException("No autenticado: el token es inválido")))
                        .onStatus(status -> status.value() == 404,
                                response-> Mono.error(new RuntimeException("Desastre no encontrado con ID" + id)))
                        .bodyToMono(HechoExternoDTO.class)
        );

    }




}
