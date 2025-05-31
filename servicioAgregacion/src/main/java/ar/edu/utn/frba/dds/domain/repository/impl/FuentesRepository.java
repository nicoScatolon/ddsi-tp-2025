package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FuentesRepository implements IFuentesRepository {
    List<Fuente> fuentes = new ArrayList<Fuente>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Fuente findById(Long id) {
        return fuentes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Fuente> findAll() {
        return fuentes;
    }

    @Override
    public void agregarFuente(Fuente fuente) {
        fuente.setId(idGenerator.getAndIncrement());
        fuentes.add(fuente);
    }

    @Override
    public void eliminarFuente(Long id) {
        fuentes.remove(this.findById(id));
    }
}
