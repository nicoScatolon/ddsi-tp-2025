package ar.edu.utn.frba.dds.domain.repository;


import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_HoraOcurrenciaPorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IE_HoraOcuPorCategoriaRepository extends JpaRepository<E_HoraOcurrenciaPorCategoria, Long> {
    List<E_HoraOcurrenciaPorCategoria> findByCodigoCategoria(String codigoCategoria);
    void deleteByFechaDeCalculoBefore(LocalDateTime fechaLimite);
}
