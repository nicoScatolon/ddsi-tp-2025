package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_HoraOcurrenciaPorCategoria;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_MayorProvinciaPorCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IE_MayorProvinciaPorCategoriaRepository extends JpaRepository<E_MayorProvinciaPorCategoria, Long> {
    List<E_MayorProvinciaPorCategoria> findByCategoria(Categoria categoria);
}
