package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@DiscriminatorValue("DINAMICA")
public class FuenteDinamica extends Fuente {
    private TipoFuente tipo = TipoFuente.DINAMICA;

    @Transient
    @JsonIgnore
    private WebClient webClient;


    private Map<Long, Hecho> mapHechos;
    private LocalDateTime ultimaActualizacion;

    public FuenteDinamica(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
        mapHechos = new HashMap<>();
    }

    public List<Hecho> updateHechos(){
        List<HechoInputDinamicaDTO> nuevosHechosDTO;
        nuevosHechosDTO = this.getHechos();
        List<Hecho> nuevosHechos = nuevosHechosDTO.stream().map(DTOConverter::convertirHechoInputDTO).toList();
        this.actualizarHechos(nuevosHechos);
        this.ultimaActualizacion = LocalDateTime.now();
        return nuevosHechos;
    }

    public List<HechoInputDinamicaDTO> getHechos() {
        return this.webClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/api/fuenteDinamica/hechos");
                    if (ultimaActualizacion != null) {
                        builder.queryParam("fechaDeCarga", ultimaActualizacion.format(DateTimeFormatter.ISO_DATE_TIME));
                    }
                    return builder.build();
                })
                .retrieve()
                .bodyToFlux(HechoInputDinamicaDTO.class)
                .collectList()
                .blockOptional()
                .orElse(Collections.emptyList());
    }

    public void actualizarHechos(List<Hecho> hechosNuevos){
        for (Hecho hechoActual : hechosNuevos){
            Hecho hechoExistente = mapHechos.get( hechoActual.getOrigenId() );

            if (hechoExistente == null) {
                // no existe, lo cargamos
                mapHechos.put(hechoActual.getOrigenId(), hechoActual);
            } else {
                //existe -> mismo origenId, lo actualizamos
                hechoActual.setId(hechoExistente.getId());
                mapHechos.put(hechoActual.getOrigenId(), hechoActual);
            }
        }
    }
}
