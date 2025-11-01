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
import java.util.ArrayList;
import java.util.Collections;
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
    private List<Hecho> listaHechos; //como hay hechos de proxy que se consumen, no tienen id local -> no puedo hacer map con origenID
    @Column(name = "ultimaActualizacion")
    private LocalDateTime ultimaActualizacion;

    public FuenteProxy(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
        listaHechos = new ArrayList<>();
    }

    public List<Hecho> updateHechos(List<Hecho> hechosPersistidosFuente){
        List<HechoInputProxyDTO> nuevosHechosDTO;
        nuevosHechosDTO = this.getHechos();
        List<Hecho> nuevosHechos = nuevosHechosDTO.stream().map(DTOConverter::convertirHechoInputDTO).peek(h -> h.setFuente(this)).toList();

        this.actualizarHechos(nuevosHechos, hechosPersistidosFuente);
        this.ultimaActualizacion = LocalDateTime.now();
        return nuevosHechos;
    }

    @Transient
    public List<HechoInputProxyDTO> getHechos() {
        if (this.webClient == null && this.url != null) {
            this.webClient = WebClient.builder().baseUrl(this.url).build();
        }

        return Objects.requireNonNull(this.webClient.get()
                .uri("/api/fuenteProxy/hechos")
                .retrieve()
                .bodyToFlux(HechoInputProxyDTO.class)
                .collectList()
                .blockOptional()
                .orElse(Collections.emptyList()));
    }


    public void actualizarHechos(List<Hecho> hechosNuevos, List<Hecho> hechosPersistidosFuente) {
        for (Hecho hechoActual : hechosNuevos) {
            Hecho hechoExistente = hechosPersistidosFuente.stream().filter(h -> compararHechos(h, hechoActual)).findFirst().orElse(null);

            if (hechoExistente != null ) {
                hechoActual.setId(hechoExistente.getId());
            }
        }
    }

    private Boolean compararHechos(Hecho h1, Hecho h2){
        Boolean origenIdIgual = h1.getOrigenId().equals(h2.getOrigenId());
        if (!origenIdIgual) {return false;}
        Boolean TituloIgual = h1.getTitulo().equals(h2.getTitulo());
        Boolean descripcionIgual = h1.getDescripcion().equals(h2.getDescripcion());
        Boolean ocurrenciaIgual = h1.getFechaDeOcurrencia().equals(h2.getFechaDeOcurrencia());
        return (TituloIgual || descripcionIgual || ocurrenciaIgual);
    }
}