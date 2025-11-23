package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_MayorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface IE_MayorCategoriaRepository extends JpaRepository<E_MayorCategoria, Long> {
    void deleteByFechaDeCalculoBefore(LocalDateTime fechaLimite);
    E_MayorCategoria findTopByOrderByFechaDeCalculoDesc();
}
