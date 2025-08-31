package ar.edu.utn.frba.dds.domain.repository.impl;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import ar.edu.utn.frba.dds.domain.repository.IEtiquetasRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EtiquetaRepository implements IEtiquetasRepository {
    private final Map<Long, Etiqueta> etiquetas = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<Etiqueta> findAll() {
        return this.etiquetas.values().stream().toList();
    }

    @Override
    public Etiqueta save(Etiqueta etiqueta) {
        etiqueta.setId(idGenerator.getAndIncrement());
        this.etiquetas.put(etiqueta.getId(), etiqueta);
        return etiqueta;
    }

    @Override
    public void delete(Long id) {
        this.etiquetas.remove(id);
    }

    @Override
    public Etiqueta findByID(Long id) {
        return etiquetas.get(id);
    }
}
