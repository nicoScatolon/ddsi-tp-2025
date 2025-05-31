package ar.edu.utn.frba.dds.domain.repository.impl;


import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.repository.ICategoriasRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CategoriasRepository implements ICategoriasRepository {
    private final Map<String,Categoria> categorias = new ConcurrentHashMap<>();


    @Override
    public List<Categoria> findAll() {
        return this.categorias;
    }

    @Override
    public Categoria save(String id, String categoriaNombre) { //para guardar nuevas categorias
        Categoria categoria = new Categoria(id, categoriaNombre);
        this.categorias.put(id, categoria);
        return categoria;
    }

    @Override
    public Categoria update(String id, String categoriaNombre){
        Categoria categoria = categorias.get(id);
        categoria.setNombre(categoriaNombre);
        categorias.put(id, categoria);
        return categoria;
    }

    @Override
    public void delete(String id) {
        this.categorias.remove(id);
    }

    @Override
    public Categoria findByID(String id) {
        return categorias.get(id);
    }
}