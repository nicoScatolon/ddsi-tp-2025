package ar.edu.utn.frba.dds.domain.entities.Fuentes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class Fuente {
    private String nombre;
    private TipoFuente tipo;
    private String url;

    @Value("${puertoFuenteEstatica}")
    private int puertoEstatica;

    @Value("${puertoFuenteDinamica}")
    private int puertoDinamica;

    @Value("${puertoFuenteProxy}")
    private int puertoProxy;

    public Fuente() {}

    public Fuente(String nombre, TipoFuente tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.url = construirBaseURL(tipo);
    }

    private String construirBaseURL(TipoFuente tipo) {
    String baseURL = "http://localhost:";
        return switch (tipo) {
            case ESTATICA -> baseURL + puertoEstatica;
            case DINAMICA -> baseURL + puertoDinamica;
            case PROXY    -> baseURL + puertoProxy;
        };
    }
}
