package ar.edu.utn.frba.dds.services.impl;

import ar.edu.utn.frba.dds.services.IServiceEstadisticas;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServiceEstadisticas implements IServiceEstadisticas {
    //repository
    @Value("agregador.base-url")
    private String urlAgregador;

}
