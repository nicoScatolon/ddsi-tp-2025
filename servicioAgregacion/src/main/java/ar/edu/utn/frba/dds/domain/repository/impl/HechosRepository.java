package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class HechosRepository implements IHechosRepository {
    private final Map<Long, IHecho> hechos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<IHecho> findAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public IHecho findById(Long id) {
        return hechos.get(id);
    }

    @Override
    public void saveAll(List<IHecho> nuevosHechos) {
        for (IHecho hecho : nuevosHechos) {
            IHecho hechoVerificado = verificarExistenteYAsignarId(hecho);
            hechos.put(hechoVerificado.getId(), hechoVerificado);
        }
    }

    @Override
    public void delete(IHecho hecho) {
        hechos.remove(hecho.getId());
    }

    @Override
    public Optional<IHecho> findByFuenteID(Long fuenteID, Class<? extends IHecho> claseEsperada) {
        return hechos.values().stream()
                .filter(h -> claseEsperada.equals(h.getClass()) &&
                        Objects.equals(h.getFuenteId(), fuenteID))
                .findFirst();
    }

    private IHecho verificarExistenteYAsignarId(IHecho hecho) {
        Optional<IHecho> existente = this.findByFuenteID(hecho.getFuenteId(), hecho.getClass());

        existente.ifPresent(hechoExistente -> hechos.remove(hechoExistente.getId()));

        if (hecho.getId() == null) {
            hecho.setId(idGenerator.getAndIncrement());
        }

        return hecho;
    }
}
