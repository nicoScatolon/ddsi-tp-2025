package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.repository.IHechosRepository;
import org.springframework.stereotype.Repository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class HechosRepository implements IHechosRepository {
    private final Map<Long, HechoBase> hechos = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<HechoBase> findAll() {
        return this.filtrarEliminados(new ArrayList<>(hechos.values()));
    }

    @Override
    public HechoBase findById(Long id) {
        return hechos.get(id);
    }

    @Override
    public void saveAll(List<HechoBase> nuevosHechos) {
        for (HechoBase hecho : nuevosHechos) {
            HechoBase hechoVerificado = verificarExistenteYAsignarId(hecho);
            hechos.put(hechoVerificado.getId(), hechoVerificado);
        }
    }

    @Override
    public void delete(HechoBase hecho) {
        hechos.remove(hecho.getId());
    }

    @Override
    public Optional<HechoBase> findByFuenteID(Long fuenteID, Class<? extends HechoBase> claseEsperada) {
        return hechos.values().stream()
                .filter(h -> claseEsperada.equals(h.getClass()) &&
                        Objects.equals(h.getFuenteId(), fuenteID))
                .findFirst();
    }

    private HechoBase verificarExistenteYAsignarId(HechoBase hecho) {
        Optional<HechoBase> existente = this.findByFuenteID(hecho.getFuenteId(), hecho.getClass());

        existente.ifPresent(hechoExistente -> hechos.remove(hechoExistente.getId()));

        if (hecho.getId() == null) {
            hecho.setId(idGenerator.getAndIncrement());
        }

        return hecho;
    }

    private List<HechoBase> filtrarEliminados(List<HechoBase> hechos) {
        return hechos.stream()
                .filter(h -> h != null && Boolean.FALSE.equals(h.getFueEliminado()))
                .collect(Collectors.toList());
    }
}
