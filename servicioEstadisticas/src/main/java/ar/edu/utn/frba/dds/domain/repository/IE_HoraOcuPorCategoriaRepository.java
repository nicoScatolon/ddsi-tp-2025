package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_HoraOcurrenciaPorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IE_HoraOcuPorCategoriaRepository extends JpaRepository<E_HoraOcurrenciaPorCategoria, Long> {
    List<E_HoraOcurrenciaPorCategoria> findByCategoria(Categoria categoria);
}
