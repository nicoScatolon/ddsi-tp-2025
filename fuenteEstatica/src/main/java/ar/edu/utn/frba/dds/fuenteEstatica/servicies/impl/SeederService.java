package ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.impl.HechosRepository;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.ISeederService;
import org.springframework.beans.factory.annotation.Value;

public class SeederService implements ISeederService {

    @Value("${source.static.csv.path}")
    private String csvPath;

    private final HechosRepository hechoRepository;

    public SeederService(HechosRepository hechoRepository) {
        this.hechoRepository = hechoRepository;
    }

    public void init () {
    }
}
