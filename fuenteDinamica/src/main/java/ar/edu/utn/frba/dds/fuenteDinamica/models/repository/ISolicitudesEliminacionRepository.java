package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.SolicitudEliminacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudesEliminacionRepository extends JpaRepository<SolicitudEliminacion, Long> {
}
