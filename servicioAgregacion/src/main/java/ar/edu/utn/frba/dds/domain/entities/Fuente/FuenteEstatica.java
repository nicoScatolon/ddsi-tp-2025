package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputEstaticaDTO;

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
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("ESTATICA")
public class FuenteEstatica extends Fuente {
    private TipoFuente tipo = TipoFuente.ESTATICA;

    @Transient
    @JsonIgnore
    private WebClient webClient;

    @Transient
    @JsonIgnore
    private Map<Long, Hecho> mapHechos = new HashMap<>();

    @Column(name = "ultimaActualizacion")
    private LocalDateTime ultimaActualizacion;

    public FuenteEstatica(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
        mapHechos = new HashMap<>();
    }

    public List<Hecho> updateHechos(List<Hecho> hechosPersistidosFuente){
        List<HechoInputEstaticaDTO> nuevosHechosDTO;
        nuevosHechosDTO = this.getHechos();
        List<Hecho> nuevosHechos = nuevosHechosDTO.stream().map(DTOConverter::convertirHechoInputDTO).peek(h -> h.setFuente(this)).toList();

        this.actualizarHechos(nuevosHechos, hechosPersistidosFuente);
        this.ultimaActualizacion = LocalDateTime.now();
        return nuevosHechos;
    }

    @Transient
    public List<HechoInputEstaticaDTO> getHechos() {
        if (this.webClient == null && this.url != null) {
            this.webClient = WebClient.builder().baseUrl(this.url).build();
        }

        return this.webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/fuenteEstatica/hechos");
                    if (ultimaActualizacion != null) {
                        uriBuilder.queryParam("fechaDeCarga",
                                ultimaActualizacion.format(DateTimeFormatter.ISO_DATE_TIME));
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(HechoInputEstaticaDTO.class)
                .collectList()
                .blockOptional()
                .orElse(Collections.emptyList());


    }

    public void actualizarHechos(List<Hecho> hechosNuevos, List<Hecho> hechosPersistidosFuente){
        List<Long> listaOrigenesId = hechosPersistidosFuente.stream().map(Hecho::getOrigenId).toList();

        for (Hecho hechoActual : hechosNuevos){
            if ( listaOrigenesId.contains( hechoActual.getOrigenId() ) ) { // si el origenId ya esta en la base de datos, es que estoy actualizando
                Hecho hechoExistente = hechosPersistidosFuente.stream()
                        .filter(h -> Objects.equals(h.getOrigenId(), hechoActual.getOrigenId()))
                        .findFirst()
                        .get(); // por la verificacion anterior, no puede ser nulo
                hechoActual.setId(hechoExistente.getId());
            }
            // Si el hecho ya existe lo actualizamos (le ponemos el id del existente), y sino lo cargamos normalmente
        }
    }
}
