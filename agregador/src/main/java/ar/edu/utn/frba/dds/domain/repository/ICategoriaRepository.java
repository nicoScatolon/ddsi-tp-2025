package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;

import java.util.List;

public interface ICategoriaRepository {
    public List<Categoria> findAll();

    public void save(Categoria categoria);

    public Categoria findByHash(Integer hash);
}
