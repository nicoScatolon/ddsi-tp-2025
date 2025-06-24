package ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.PaginaHechosResponseDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente.interfacesDeCapacidad.ServidoraDeHechosPorId;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class FuenteDDS implements ServidoraDeHechosPorId {
    private Long Id;
    private TipoFuenteProxy tipoFuenteProxy= TipoFuenteProxy.EXTERNA;
    private final WebClient webClient;
    private final String token;
    private final String baseUrl;


    public FuenteDDS(@Value("${api.ddsi.base-url}") String baseUrl,
                     @Value("${api.ddsi.token}") String token) {
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.token = token;
    }



    @Override
    public Mono<List<HechoExternoDTO>> getHechos() {
        return webClient.get()
                .uri("/api/desastres?page=1")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
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


                });

    }




    public Mono<HechoExternoDTO> getHechoPorId(Long id) {
        return webClient.get()
                .uri("/api/desastres/{id}", id)
                .headers(h-> h.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        response-> Mono.error(new RuntimeException("Desastre no encontrado con ID " + id)))
                .bodyToMono(HechoExternoDTO.class);

    }

}
