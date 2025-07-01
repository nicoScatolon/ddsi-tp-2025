package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.HechoInputDinamicaDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class FuenteDinamica implements IFuente {
    private Long id;
    private String url;
    private String nombre;
    private TipoFuente tipo = TipoFuente.DINAMICA;
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
        return nuevosHechos;
    }

    public List<HechoInputDinamicaDTO> getHechos() {
        return Objects.requireNonNull(this.webClient.get()
                .uri("/hechos")
                .retrieve()
                .bodyToFlux(HechoInputDinamicaDTO.class)
                .collectList()
                .block());
        //TODO resolver el tema de actualizacion por fecha (que getHechos reciba unicamente a partir de su ultima fecha de modificación)
        // por la uri pasar como queryParam parametro la ultima fecha de actualizacion
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
