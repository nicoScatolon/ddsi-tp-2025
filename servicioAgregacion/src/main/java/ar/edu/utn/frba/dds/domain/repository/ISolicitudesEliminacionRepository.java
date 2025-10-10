package ar.edu.utn.frba.dds.domain.repository;


import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISolicitudesEliminacionRepository extends JpaRepository<SolicitudEliminarHecho, Long> {
    @Query("SELECT s FROM SolicitudEliminarHecho s WHERE s.eliminada = false")
    List<SolicitudEliminarHecho> findActives();

    SolicitudEliminarHecho findByHechoId(Long hechoId);
    List<SolicitudEliminarHecho> findAllByHechoId(Long hechoId);
    List<SolicitudEliminarHecho> findByEstado(EstadoDeSolicitud estado);
}
