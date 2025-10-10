package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findHechoByEstado(EstadoHecho estado);
    List<Hecho> findAllByFechaDeCargaAfter(LocalDateTime fechaDeCargaAfter);
    List<Hecho> findHechoByEstadoAndFechaDeCargaAfter(EstadoHecho estado, LocalDateTime fechaDeCargaAfter);
    List<Hecho> findAllByContribuyenteId(Long contribuyenteId);
    List<Hecho> findAllByContribuyenteIdAndEstado(Long contribuyenteId, EstadoHecho estado);
}
