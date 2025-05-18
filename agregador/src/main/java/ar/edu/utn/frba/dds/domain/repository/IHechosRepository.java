package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Hecho;

import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();
    Hecho findById(Long id);
    void save(Hecho hecho);
    void delete(Hecho hecho);
}
