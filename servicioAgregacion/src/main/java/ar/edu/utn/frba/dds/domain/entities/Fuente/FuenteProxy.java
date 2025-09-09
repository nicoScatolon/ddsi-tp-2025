package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputProxyDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("PROXY")
public class FuenteProxy extends Fuente {
    private TipoFuente tipo = TipoFuente.PROXY;

    @Transient
    @JsonIgnore
    private WebClient webClient;
    @Transient
    @JsonIgnore
    private List<Hecho> hechos;
    @Column(name = "ultimaActualizacion")
    private LocalDateTime ultimaActualizacion;

    public FuenteProxy(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @Transient
    public List<Hecho> getHechos() {
        if (this.webClient == null && this.url != null) {
            this.webClient = WebClient.builder().baseUrl(this.url).build();
        }

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