package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class FuenteProxy implements IFuente {
    private Long id;
    private String url;
    private String nombre;
    private TipoFuente tipo = TipoFuente.PROXY;
    private WebClient webClient;

    public FuenteProxy(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public List<HechoInputProxyDTO> getHechos() {
        return Objects.requireNonNull(this.webClient.get()
                        .uri("/hechos")
                        .retrieve()
                        .bodyToFlux(HechoInputProxyDTO.class)
                        .collectList()
                        .block());
    }
}
