package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;



import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudesRepository {
    SolicitudEliminacion save(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
    // find by procesadas
    // find by sin procesar
    // TODO
}
