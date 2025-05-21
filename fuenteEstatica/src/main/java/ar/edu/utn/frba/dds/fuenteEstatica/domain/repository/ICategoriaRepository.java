package ar.edu.utn.frba.dds.fuenteEstatica.domain.repository;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Categoria;

import java.util.List;

public interface ICategoriaRepository {
    List<Categoria> findAll();
    Categoria findByHash(Integer hash);
    void save(Categoria categoria);
    void delete(Categoria categoria);
}

// TODO es necesario un repository en la fuente estática?