package ar.edu.utn.frba.dds.domain.repository.impl;


import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CategoriasRepository implements ICategoriasRepository {
    private final List<Categoria> categorias;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CategoriasRepository() {
        categorias = new ArrayList<>();
    }

    @Override
    public List<Categoria> findAll() {
        return this.categorias;
    }

    @Override
    public Categoria save(String categoriaNombre) {
        //En caso de que una categoria exista, se devolvera la instancia para no guardar duplicados
        Categoria existente = categorias.stream()
                .filter(c -> c.getNombre().equals(categoriaNombre))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            return existente;
        }
        Categoria newCategoria = new Categoria(categoriaNombre);

        newCategoria.setId(idGenerator.getAndIncrement());
        categorias.add(newCategoria);
        return newCategoria;
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