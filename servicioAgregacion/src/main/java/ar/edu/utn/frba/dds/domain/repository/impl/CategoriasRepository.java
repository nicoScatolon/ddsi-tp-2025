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
    public Categoria save(Categoria categoria) {
        String handle = StringToHandle(categoria.getNombre());

        //En caso de que una categoria exista, se devolvera la instancia para no guardar duplicados
        Categoria existente = categorias.stream()
                .filter(c -> StringToHandle(c.getNombre()).equals(handle))
                .findFirst()
                .orElse(null);
                //ToDO: Esta mal comparar handles de categorias para saber si existe?

        if (existente != null) {
            return existente;
        }

        categoria.setId(idGenerator.getAndIncrement());
        categorias.add(categoria);
        return categoria;
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

    private String StringToHandle(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("El string no puede ser nulo ni vacío.");
        }

        return string
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
    }

}