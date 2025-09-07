package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICategoriasRepository extends JpaRepository<Categoria, String> {
    List<Categoria> findByNombre (String nombre);
}
