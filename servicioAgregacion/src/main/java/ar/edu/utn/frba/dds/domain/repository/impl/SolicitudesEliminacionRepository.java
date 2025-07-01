package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.domain.repository.ISolicitudesEliminacionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SolicitudesEliminacionRepository implements ISolicitudesEliminacionRepository {
    private final List<SolicitudEliminarHecho> solicitudesEliminacion;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public SolicitudesEliminacionRepository() {
        solicitudesEliminacion = new ArrayList<>();
    }

    @Override
    public List<SolicitudEliminarHecho> findActives() {
        return this.solicitudesEliminacion
                .stream()
                .filter(s->!s.isEliminada())
                .collect(Collectors.toList());
    }

    @Override
    public List<SolicitudEliminarHecho> findAll() {
        return this.solicitudesEliminacion;
    }

    @Override
    public SolicitudEliminarHecho findById(Long id) {
        return this.solicitudesEliminacion.stream().filter(s -> Objects.equals(s.getId(), id)).findFirst().orElse(null);
    }

    @Override
    public void save(SolicitudEliminarHecho solicitudEliminarHecho) {
        if (solicitudEliminarHecho.getId() == null) {
            solicitudEliminarHecho.setId(idGenerator.getAndIncrement());
            solicitudesEliminacion.add(solicitudEliminarHecho);
            return;
        }

        // Si existe la solicitud la reemplaza
        solicitudesEliminacion.removeIf(s -> s.getId().equals(solicitudEliminarHecho.getId()));
        solicitudesEliminacion.add(solicitudEliminarHecho);
    }


    @Override
    public void delete(SolicitudEliminarHecho solicitudEliminarHecho) {
        solicitudEliminarHecho.setEliminada(true);
    }
}
