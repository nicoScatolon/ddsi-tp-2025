package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_SolicitudesSpam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IE_SolicitudesSpamRepository extends JpaRepository<E_SolicitudesSpam, Long> {
}
