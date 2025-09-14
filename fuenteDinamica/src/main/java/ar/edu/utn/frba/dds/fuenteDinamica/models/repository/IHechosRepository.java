package ar.edu.utn.frba.dds.fuenteDinamica.models.repository;

import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoSolicitudEliminacion;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByEstado(EstadoSolicitudEliminacion estado);

    List<Hecho> findByEstadoOrderByFechaDeCargaDesc(EstadoHecho estado);
}
