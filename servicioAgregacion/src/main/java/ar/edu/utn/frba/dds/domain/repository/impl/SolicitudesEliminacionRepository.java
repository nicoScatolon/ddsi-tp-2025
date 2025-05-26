package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.domain.repository.ISolicitudesEliminacionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class SolicitudesEliminacionRepository implements ISolicitudesEliminacionRepository {
    private final List<SolicitudEliminarHecho> solicitudesEliminacion;
    public SolicitudesEliminacionRepository() {
        solicitudesEliminacion = new ArrayList<>();
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
        this.solicitudesEliminacion.add(solicitudEliminarHecho);
    }

    @Override
    public void delete(SolicitudEliminarHecho solicitudEliminarHecho) {
        this.solicitudesEliminacion.remove(solicitudEliminarHecho);
    }
}
