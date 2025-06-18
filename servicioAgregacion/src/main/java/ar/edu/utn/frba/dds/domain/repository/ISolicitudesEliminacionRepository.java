package ar.edu.utn.frba.dds.domain.repository;


import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;

import java.util.List;

public interface ISolicitudesEliminacionRepository {
    List<SolicitudEliminarHecho> findAll();
    SolicitudEliminarHecho findById(Long id);
    void save(SolicitudEliminarHecho solicitudEliminarHecho);
    void delete(SolicitudEliminarHecho solicitudEliminarHecho);
    List<SolicitudEliminarHecho> findActives();
}
