package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_MayorProvinciaPorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IE_MayorProvinciaPorCategoriaRepository extends JpaRepository<E_MayorProvinciaPorCategoria, Long> {
}
