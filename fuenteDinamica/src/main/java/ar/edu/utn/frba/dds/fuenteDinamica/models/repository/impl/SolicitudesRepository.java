package ar.edu.utn.frba.dds.fuenteDinamica.models.repository.impl;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudEliminacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.repository.ISolicitudesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SolicitudesRepository implements ISolicitudesRepository {
    private final Map<Long, SolicitudEliminacion> solicitudes = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public SolicitudEliminacion save(SolicitudEliminacion solicitud) {
        if (solicitud.getId() == null) { // no esta cargado
            Long id = idGenerator.getAndIncrement();
            solicitud.setId(id);
            solicitudes.put(id, solicitud);
        } else { // esta cargado -> actualizo
            solicitudes.put(solicitud.getId(), solicitud);
        }
        return solicitud;
    }

    @Override
    public List<SolicitudEliminacion> findAll() {
        return new ArrayList<>(solicitudes.values());
    }
}
