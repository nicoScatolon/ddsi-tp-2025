package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;

import java.util.List;

public interface ICategoriaRepository {
    List<Categoria> findAll();
    Categoria findByHash(Integer hash);
    void save(Categoria categoria);
    void delete(Categoria categoria);
}
