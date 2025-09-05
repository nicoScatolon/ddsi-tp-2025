package ar.edu.utn.frba.dds.domain.entities.Fuente;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_fuente")
@Table(name = "fuentes")
public abstract class Fuente implements IFuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, length = 120)
    protected String nombre;

    @Column(nullable = false)
    protected String url;


    @Transient
    @JsonIgnore
    protected WebClient webClient;

    @Transient
    @JsonIgnore
    protected Map<Long, Hecho> mapHechos = new HashMap<>();

    @PostLoad
    @PostPersist
    private void initWebClient() {
        if (this.url != null && this.webClient == null) {
            this.webClient = WebClient.builder().baseUrl(this.url).build();
        }
        if (this.mapHechos == null) {
            this.mapHechos = new HashMap<>();
        }
    }
}
