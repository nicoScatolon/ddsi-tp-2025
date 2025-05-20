package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechosRepository implements IHechosRepository {
    private List<Hecho> hechos;
    private final AtomicLong idGenerator = new AtomicLong(1);

    //ToDO: Hay que implementarlo para las bases de datos, esto simplemente lo guarda en memoria
    public HechosRepository() {
        hechos = new ArrayList<Hecho>();
    } //ToDo: se debería reemplazar por DB(?

    @Override
    public List<Hecho> findAll() {
        return this.hechos;
    }

    @Override
    public Hecho findById(Long id) {
        return this.hechos.stream().filter(h -> h.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void save(Hecho hecho) {
        Long id = idGenerator.getAndIncrement();
        this.hechos.add(hecho);
    }

    @Override
    public void delete(Hecho hecho) {
        this.hechos.remove(hecho);
    }
}
