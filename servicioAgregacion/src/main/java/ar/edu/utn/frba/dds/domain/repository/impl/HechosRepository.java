package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class HechosRepository implements IHechosRepository {
    private final Map<Long, Hecho> hechos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Hecho> findAll() {
        return this.filtrarEliminados(new ArrayList<>(hechos.values()));
    }

    @Override
    public Hecho findById(Long id) {
        return hechos.get(id);
    }

    @Override
    public void saveAll(List<Hecho> nuevosHechos) {
        for (Hecho hecho : nuevosHechos) {
            if (hecho.getId() == null) { hecho.setId(idGenerator.getAndIncrement()); }
            hechos.put(hecho.getId(), hecho);
        }
    }

    @Override
    public void delete(Hecho hecho) {
        hechos.remove(hecho.getId());
    }

    //TODO esto en la base de datos puede hacerse con un SELECT con un WHERE
    private List<Hecho> filtrarEliminados(List<Hecho> hechos) {
        if (hechos == null) return Collections.emptyList();
        return hechos.stream()
                .filter(Objects::nonNull)
                .filter(h -> Boolean.FALSE.equals(h.getFueEliminado()))
                .toList();
    }
}
