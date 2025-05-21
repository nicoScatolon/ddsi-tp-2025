package ar.edu.utn.frba.dds.domain.repository.impl;


import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CategoriasRepository implements ICategoriasRepository {
    private List<Categoria> categorias;
    public CategoriasRepository() {
        categorias = new ArrayList<Categoria>(); //ToDo: se debería reemplazar por DB(?
    }

    @Override
    public List<Categoria> findAll() {
        return this.categorias;
    }

    @Override
    public void save(Categoria categoria) {
        categoria.setId(categoria.getId());
        this.categorias.add(categoria);
    }

    @Override
    public void delete(Categoria categoria) {
        this.categorias.remove(categoria);
    }

    @Override
    public Categoria findByID(Long id) {
        return this.categorias
                .stream()
                .filter(categoria -> Objects.equals(categoria.getId(),id))
                .findFirst()
                .orElse(null);
    }
}