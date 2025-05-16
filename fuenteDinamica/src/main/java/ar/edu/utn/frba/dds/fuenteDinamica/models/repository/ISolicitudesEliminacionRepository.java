package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;



import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;

import java.util.List;

public interface ISolicitudesEliminacionRepository {
    SolicitudEliminacion save(SolicitudEliminacion solicitud);
    List<SolicitudEliminacion> findAll();
    SolicitudEliminacion remove(SolicitudEliminacion solicitud);
    // find by procesadas
    // find by sin procesar
    // TODO
}
