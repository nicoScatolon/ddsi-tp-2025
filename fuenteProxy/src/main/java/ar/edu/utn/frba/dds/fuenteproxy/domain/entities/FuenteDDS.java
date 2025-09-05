package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoExternoDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.PaginaHechosResponseDdsDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.interfacesDeCapacidad.ServidoraDeHechosPorId;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


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


    public FuenteDDS(String baseUrl, String token) {
        this.setBaseUrl(baseUrl);
        this.token = token;
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }



    @Override
    @Transient
    public Mono<List<HechoExternoDTO>> getHechos() {
        return webClient.get()
                .uri("/api/desastres?page=1")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(PaginaHechosResponseDdsDTO.class)
                .flatMap(primerPagina -> {
                    int lastPage = primerPagina.getLastPage();
                    List<HechoExternoDTO> hechosTotales = new ArrayList<>(primerPagina.getData());

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
