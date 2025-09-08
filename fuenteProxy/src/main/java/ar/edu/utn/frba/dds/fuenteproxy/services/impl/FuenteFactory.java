package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteMetaMapa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FuenteFactory {
    @Value("${api.metamapa.base-url}")
    private String metamapaBaseUrl;

    @Value("${api.ddsi.base-url}")
    private String ddsBaseUrl;

    @Value("${api.ddsi.token}")
    private String ddsToken;

    public FuenteMetaMapa nuevaFuenteMetaMapa(String nombre) {

        return new FuenteMetaMapa(nombre,metamapaBaseUrl);
    }

    public FuenteDDS nuevaFuenteDDS(String nombre) {
        return new FuenteDDS(nombre, ddsBaseUrl, ddsToken);
    }

}
