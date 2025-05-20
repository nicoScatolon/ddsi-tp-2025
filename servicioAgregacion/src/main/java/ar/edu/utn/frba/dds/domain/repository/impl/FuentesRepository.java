package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.TipoFuente;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FuentesRepository implements IFuentesRepository {
    @Value("${puertoFuenteEstatica}")
    private int puertoEstatica;

    @Value("${puertoFuenteDinamica}")
    private int puertoDinamica;

    @Value("${puertoFuenteProxy}")
    private int puertoProxy;

    @Override
    public List<Fuente> obtenerFuentes() { //ToDO está hardCodeado, se puede mejorar
        String baseURL = "http://localhost:";
        return List.of(
                new Fuente("Fuente Estática", TipoFuente.ESTATICA, baseURL + puertoEstatica),
                new Fuente("Fuente Dinámica", TipoFuente.DINAMICA, baseURL + puertoDinamica),
                new Fuente("Fuente Proxy", TipoFuente.PROXY, baseURL + puertoProxy)
        );
    }
}
