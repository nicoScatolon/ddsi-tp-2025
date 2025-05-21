package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechosRepository implements IHechosRepository {
    private final Map<Long, Hecho> hechos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Hecho> findAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public Hecho findById(Long id) {
        return hechos.get(id);
    }

    @Override
    public void saveAll(List<Hecho> nuevosHechos) {
        for (Hecho hecho : nuevosHechos) {
            Hecho hechoVerificado = verificarExistenteYAsignarId(hecho);
            hechos.put(hechoVerificado.getId(), hechoVerificado);
        }
    }

    @Override
    public void delete(Hecho hecho) {
        hechos.remove(hecho.getId());
    }

    @Override
    public Optional<Hecho> findByFuente(Long fuenteID, String fuenteNombre) {
        return hechos.values().stream()
                .filter(h -> Objects.equals(h.getFuenteId(), fuenteID) &&
                        Objects.equals(h.getFuenteNombre(), fuenteNombre))
                .findFirst();
    }

    private Hecho verificarExistenteYAsignarId(Hecho hecho) {
        Optional<Hecho> existente = this.findByFuente(hecho.getFuenteId(), hecho.getFuenteNombre());

        existente.ifPresent(hechoExistente -> hechos.remove(hechoExistente.getId()));

        if (hecho.getId() == null) {
            hecho.setId(idGenerator.getAndIncrement());
        }

        return hecho;
    }
}
