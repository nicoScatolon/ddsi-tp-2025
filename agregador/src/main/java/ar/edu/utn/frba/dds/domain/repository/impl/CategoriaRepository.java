package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class CategoriaRepository implements ICategoriaRepository {
    private List<Categoria> categorias;
    public CategoriaRepository() {
        categorias = new ArrayList<Categoria>(); //ToDo: se debería reemplazar por DB(?
    }

    @Override
    public List<Categoria> findAll() {
        return this.categorias;
    }

    @Override
    public void save(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.setHash(categoria.getHash());
    }

    @Override
    public Categoria findByHash(Integer hash) {
        return this.categorias
                .stream()
                .filter(categoria -> Objects.equals(categoria.getHash(),hash))
                .findFirst()
                .orElse(null);
    }
}
