package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

public class FuenteEstatica extends Fuente {

    public FuenteEstatica(){
        this.tipo = TipoFuente.ESTATICA;
    }

    @Override
    public List<IHechoInputDTO> getHechos(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.baseUrl(this.url).build();
        return Objects.requireNonNull(webClient.get()
                        .uri("/hechos")
                        .retrieve()
                        .bodyToFlux(HechoInputEstaticaDTO.class)
                        .collectList()
                        .block())
                .stream()
                .map(dto -> (IHechoInputDTO) dto)
                .toList();
    }
}
