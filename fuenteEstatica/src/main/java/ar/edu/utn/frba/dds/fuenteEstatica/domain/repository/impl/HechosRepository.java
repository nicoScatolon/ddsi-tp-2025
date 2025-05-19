package ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.impl;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.repository.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class HechosRepository implements IHechosRepository {
    private List<Hecho> hechos;

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
        return this.hechos.stream().filter(h -> Objects.equals(h.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public void save(Hecho hecho) {
        hecho.setId((long) this.hechos.size()); //ToDO: Modificar, pueden haber hechos con mismo id
        this.hechos.add(hecho);
    }

    @Override
    public void delete(Hecho hecho) {
        this.hechos.remove(hecho);
    }
}