package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class FuenteEstatica implements IFuente {
    private Long id;
    private String url;
    private String nombre;
    private TipoFuente tipo = TipoFuente.ESTATICA;
    private WebClient webClient;

    public FuenteEstatica(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public List<HechoInputEstaticaDTO> getHechos() {
        return Objects.requireNonNull(this.webClient.get()
                        .uri("/hechos")
                        .retrieve()
                        .bodyToFlux(HechoInputEstaticaDTO.class)
                        .collectList()
                        .block());
    }
}
