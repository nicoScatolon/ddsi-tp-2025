package ar.edu.utn.frba.dds.clienteGrafico.services.impl;

import ar.edu.utn.frba.dds.clienteGrafico.services.IEstadisticasService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EstadisticasService implements IEstadisticasService {
    private final WebClient webClient;

    public EstadisticasService(@Qualifier("estadisticasWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

}
