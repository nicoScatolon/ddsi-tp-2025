package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import java.util.List;

public interface IHechosRepository {
    Hecho save(Hecho hecho);
    List<Hecho> findAll();
}
