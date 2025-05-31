package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

public class FuenteProxy extends Fuente {

    public FuenteProxy(){
        this.tipo = TipoFuente.PROXY;
    }

    @Override
    public List<IHechoInputDTO> getHechos(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.baseUrl(this.url).build();
        return Objects.requireNonNull(webClient.get()
                        .uri("/hechos")
                        .retrieve()
                        .bodyToFlux(HechoInputProxyDTO.class)
                        .collectList()
                        .block())
                .stream()
                .map(dto -> (IHechoInputDTO) dto)
                .toList();
    }
}
