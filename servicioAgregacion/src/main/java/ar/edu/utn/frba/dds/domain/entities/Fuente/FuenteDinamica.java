package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class FuenteDinamica implements IFuente {
    private Long id;
    private String url;
    private String nombre;
    private TipoFuente tipo = TipoFuente.DINAMICA;
    private WebClient webClient;

    public FuenteDinamica(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public List<HechoInputDinamicaDTO> getHechos() {
        return Objects.requireNonNull(this.webClient.get()
                        .uri("/hechos")
                        .retrieve()
                        .bodyToFlux(HechoInputDinamicaDTO.class)
                        .collectList()
                        .block());
    }
}
