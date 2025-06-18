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
            Hecho hechoVerificado = verificarExistenteYAsignarId(hecho);
            hechos.put(hechoVerificado.getId(), hechoVerificado);
        }
    }

    @Override
    public void delete(Hecho hecho) {
        hechos.remove(hecho.getId());
    }

    public Optional<Hecho> findByFuenteAndId(IFuente fuente, Long idOrigenHecho) {
        //verifico los hechos con el mismo id de su origen que provengan de la misma fuente
        return hechos.values().stream()
                .filter(h -> Objects.equals(h.getOrigenId(), idOrigenHecho) && h.getFuente().equals(fuente))
                .findFirst();
    }

    private Hecho verificarExistenteYAsignarId(Hecho hecho) {
        Optional<Hecho> existente = this.findByFuenteAndId(hecho.getFuente(), hecho.getOrigenId());

        existente.ifPresent(hechoExistente -> hechos.remove(hechoExistente.getId()));

        if (hecho.getId() == null) {
            hecho.setId(idGenerator.getAndIncrement());
        }

        return hecho;
    }

    private List<Hecho> filtrarEliminados(List<Hecho> hechos) {
        return hechos.stream()
                .filter(h -> h != null && Boolean.FALSE.equals(h.getFueEliminado()))
                .collect(Collectors.toList());
    }
}
