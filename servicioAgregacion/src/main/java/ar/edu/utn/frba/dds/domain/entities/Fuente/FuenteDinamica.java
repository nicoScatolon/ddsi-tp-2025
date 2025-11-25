package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.Column;
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
import java.util.stream.Collectors;

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

    @Column(name = "ultimaActualizacion")
    private LocalDateTime ultimaActualizacion;

    public FuenteDinamica(String url) {
        this.url = url;
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public List<Hecho> updateHechos(List<Hecho> hechosPersistidosFuente){
        List<HechoInputDinamicaDTO> nuevosHechosDTO;
        nuevosHechosDTO = this.getHechos();
        List<Hecho> nuevosHechos = nuevosHechosDTO.stream()
                .map(DTOConverter::convertirHechoInputDTO)
                .peek(h -> h.setFuente(this))
                .collect(Collectors.toCollection(ArrayList::new));

        this.actualizarHechos(nuevosHechos, hechosPersistidosFuente);

        this.ultimaActualizacion = LocalDateTime.now();
        return nuevosHechos;
    }

    @Transient
    public List<HechoInputDinamicaDTO> getHechos() {
        if (this.webClient == null && this.url != null) {
            this.webClient = WebClient.builder().baseUrl(this.url).build();
        }
        return this.webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder = uriBuilder.path("/api/fuenteDinamica/hechos/privada");
                    if (ultimaActualizacion != null) {
                        uriBuilder.queryParam("fechaDeGestion", ultimaActualizacion.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    }
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(HechoInputDinamicaDTO.class)
                .collectList()
                .blockOptional()
                .orElse(Collections.emptyList());
    }


    public void actualizarHechos(List<Hecho> hechosNuevos, List<Hecho> hechosPersistidosFuente){
        List<Long> listaOrigenesId = hechosPersistidosFuente.stream().map(Hecho::getOrigenId).toList();

        List<Hecho> hechosAEliminar = new ArrayList<>();
        List<Hecho> hechosAActualizar = new ArrayList<>();

        for (Hecho hechoActual : hechosNuevos){
            if ( listaOrigenesId.contains( hechoActual.getOrigenId() ) ) { // si el origenId ya esta en la base de datos, es que estoy actualizando
                Hecho hechoExistente = hechosPersistidosFuente.stream()
                        .filter(h -> Objects.equals(h.getOrigenId(), hechoActual.getOrigenId()))
                        .findFirst()
                        .get(); // por la verificacion anterior, no puede ser nulo

                hechoExistente.actualizarse(hechoActual);
                hechosAActualizar.add(hechoExistente);
                hechosAEliminar.add(hechoActual);
            }
        }

        hechosNuevos.removeAll(hechosAEliminar);
        hechosNuevos.addAll(hechosAActualizar);
    }
}
