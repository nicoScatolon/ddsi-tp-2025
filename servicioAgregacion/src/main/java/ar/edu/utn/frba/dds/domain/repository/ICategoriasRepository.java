package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;

import java.util.List;

public interface ICategoriasRepository {
    List<Categoria> findAll();
    Categoria findByID(Long id);
    Categoria save(String nombre);
    void delete(Categoria categoria);
}
