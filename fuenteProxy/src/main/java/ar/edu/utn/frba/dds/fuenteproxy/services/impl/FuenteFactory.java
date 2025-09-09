package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.FuenteMetaMapa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FuenteFactory {


    @Value("${api.ddsi.base-url}")
    private String ddsBaseUrl;



    public FuenteMetaMapa nuevaFuenteMetaMapa(String nombre, String baseUrl) {

        return new FuenteMetaMapa(nombre,baseUrl);
    }

    public FuenteDDS nuevaFuenteDDS(String nombre) {
        return new FuenteDDS(nombre, ddsBaseUrl);
    }

}
