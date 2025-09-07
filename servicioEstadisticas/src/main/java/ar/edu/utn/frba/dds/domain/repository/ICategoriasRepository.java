package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoriasRepository extends JpaRepository<Categoria, String> {
}
