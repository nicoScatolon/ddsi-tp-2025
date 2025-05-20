package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.TipoFuente;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FuentesRepository implements IFuentesRepository {


    @Override
    public List<Fuente> obtenerFuentes() { //ToDO está hardCodeado, se puede mejorar
        String baseURL = "http://localhost:";
        return List.of(
                new Fuente("Fuente Estática", TipoFuente.ESTATICA),
                new Fuente("Fuente Dinámica", TipoFuente.DINAMICA),
                new Fuente("Fuente Proxy", TipoFuente.PROXY)
        );
    }
}
