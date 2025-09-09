package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEquivalenteCatRepository extends JpaRepository<EquivalenteCategoria, Long> {

    boolean existsByEquivalente(String equivalente);


    EquivalenteCategoria findWithCategoriaByEquivalente(String equivalente);


    Optional<String> findNombreCategoriaByEquivalente(String equivalente);

    void deleteByEquivalente(String equivalente);
}
