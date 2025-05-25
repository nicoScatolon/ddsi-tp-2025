package ar.edu.utn.frba.dds.fuenteDinamica.models.repository.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.IHechosRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class HechosRepository implements IHechosRepository {
    private final Map<Long, Hecho> hechos = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();


    @Override
    public Hecho save(Hecho hecho) {
        if (hecho.getId() == null) { // no esta cargado
            Long id = idGenerator.getAndIncrement();
            hecho.setId(id);
            hechos.put(id, hecho);
        } else { // esta cargado -> actualizo
            hechos.put(hecho.getId(), hecho);
        }
        return hecho;
    }

    @Override
    public List<Hecho> findAll() {
        return new ArrayList<>(hechos.values());
    }

    @Override
    public Hecho findById(Long idBuscado) {
        return hechos.get(idBuscado);
    }
}
