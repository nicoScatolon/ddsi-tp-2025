package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEquivalenteCatRepository extends JpaRepository<EquivalenteCategoria, Long> {
    void deleteByNombreEquivalente(String nombreEquivalente);

    Optional<EquivalenteCategoria> findByCategoria_CodigoCategoriaAndNombreEquivalente(String codigoCategoria, String nombreEquivalente);

    boolean existsByCategoria_CodigoCategoriaAndNombreEquivalente(String codigoCategoria, String nombreEquivalente);

    void deleteByCategoria_CodigoCategoriaAndNombreEquivalente(String codigoCategoria, String nombreEquivalente);
}

