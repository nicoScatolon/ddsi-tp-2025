package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Hecho;

import java.util.List;
import java.util.Optional;

public interface IHechosRepository {
    List<Hecho> findAll();
    Hecho findById(Long id);
    Optional<Hecho> findByFuente(Long fuenteID, String fuenteNombre);
    void saveAll(List<Hecho> hechos);
    void delete(Hecho hecho);
}
