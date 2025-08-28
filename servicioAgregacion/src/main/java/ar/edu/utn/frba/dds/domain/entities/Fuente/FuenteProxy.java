package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private List<Hecho> hechos;
    private LocalDateTime ultimaActualizacion = LocalDateTime.MIN;

    public FuenteProxy(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public List<Hecho> getHechos() {
        return Objects.requireNonNull(this.webClient.get()
                .uri("/api/fuenteProxy/hechos")
                .retrieve()
                .bodyToFlux(HechoInputProxyDTO.class)
                .collectList()
                .block())
                .stream()
                .map(DTOConverter::convertirHechoInputDTO)
                .toList();
    }

    public List<Hecho> updateHechos(){
        List<Hecho> hechosNuevos = getHechos();
        this.ultimaActualizacion = LocalDateTime.now();
        this.hechos.addAll(hechosNuevos);
        return this.hechos;
    }


}