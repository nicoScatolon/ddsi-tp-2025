package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;

import java.util.List;

public interface ICategoriasRepository {
    List<Categoria> findAll();
    Categoria findByID(String id);
    Categoria save(Categoria categoria);
    void delete(String id);

}
