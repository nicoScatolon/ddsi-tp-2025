package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContenidoMultimedia implements IContenidoMultimedia{
    private String url;
    private String descripcion;

    @Override
    public String getContenido() {
        return url;
    }
}
