package ar.edu.utn.frba.dds.fuenteproxy.services.impl;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteDDS;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.FuenteMetaMapa;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.TipoFuenteProxy;
import ar.edu.utn.frba.dds.fuenteproxy.domain.repositories.IFuentesRepositoryJPA;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FuenteFactory {
    private final IFuentesRepositoryJPA fuenteRepository;


    @Getter
    @Value("${api.ddsi.base-url}")
    private String ddsBaseUrl;



    public FuenteMetaMapa nuevaFuenteMetaMapa(String nombre, String baseUrl) {
        return fuenteRepository.findByTipoAndBaseUrl(TipoFuenteProxy.METAMAPA, baseUrl)
                .map(FuenteMetaMapa.class::cast)
                .orElseGet(() -> fuenteRepository.save(new FuenteMetaMapa(nombre, baseUrl)));
    }

    public FuenteDDS nuevaFuenteDDS(String nombre) {
        return fuenteRepository.findByTipoAndBaseUrl(TipoFuenteProxy.EXTERNA,ddsBaseUrl)
                .map(FuenteDDS.class::cast)
                .orElseGet(() -> fuenteRepository.save(new FuenteDDS(nombre, ddsBaseUrl)));
    }

}


