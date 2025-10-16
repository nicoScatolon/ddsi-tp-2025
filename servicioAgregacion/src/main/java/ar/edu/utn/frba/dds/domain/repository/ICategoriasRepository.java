package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ICategoriasRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    Optional<Categoria> findCategoriaByCodigoCategoria(String codigoCategoria);

    @Query("SELECT c.nombre FROM Categoria c")
    List<String> findAllNombres();
}

