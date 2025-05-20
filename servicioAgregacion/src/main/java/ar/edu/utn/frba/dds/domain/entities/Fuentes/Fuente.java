package ar.edu.utn.frba.dds.domain.entities.Fuentes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Fuente {
    private String nombre;
    private TipoFuente tipo;
    private String baseURL;

    public Fuente() {
    }

    public Fuente(String nombre, TipoFuente tipo, String baseURL) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.baseURL = baseURL;
    }
}
