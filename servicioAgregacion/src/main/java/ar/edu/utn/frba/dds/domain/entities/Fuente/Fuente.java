package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.dtos.input.hechos.IHechoInputDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Getter
@Setter
public abstract class Fuente {
    Long id;
    String url;
    String nombre;
    TipoFuente tipo;

    public List<IHechoInputDTO> getHechos(WebClient.Builder webClientBuilder) {
        return null;
    }
}
