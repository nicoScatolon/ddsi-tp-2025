package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Hecho;

import java.util.List;

public interface IHechosRepository {
    public List<Hecho> findAll();
    public Hecho findById(Long id);
    public void save(Hecho hecho);
    public void delete(Hecho hecho);
}
