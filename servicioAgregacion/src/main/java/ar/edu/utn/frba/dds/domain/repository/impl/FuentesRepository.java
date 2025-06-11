package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class FuentesRepository implements IFuentesRepository {
    List<IFuente> fuentes = new ArrayList<IFuente>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public IFuente findById(Long id) {
        return fuentes.stream().filter(n -> n.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<IFuente> findAll() {
        return fuentes;
    }

    @Override
    public void saveFuente(IFuente fuente) {
        fuente.setId(idGenerator.getAndIncrement());
        fuentes.add(fuente);
    }

    @Override
    public void deleteFuente(Long id) {
        fuentes.remove(this.findById(id));
    }
}
