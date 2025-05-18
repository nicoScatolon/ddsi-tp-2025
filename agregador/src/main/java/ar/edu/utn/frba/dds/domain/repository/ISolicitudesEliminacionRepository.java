package ar.edu.utn.frba.dds.domain.repository;


import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;

import java.util.List;

public interface ISolicitudesEliminacionRepository {
    public List<SolicitudEliminarHecho> findAll();
    public SolicitudEliminarHecho findById(Long id);
    public void save(SolicitudEliminarHecho solicitudEliminarHecho);
    public void delete(SolicitudEliminarHecho solicitudEliminarHecho);
}
