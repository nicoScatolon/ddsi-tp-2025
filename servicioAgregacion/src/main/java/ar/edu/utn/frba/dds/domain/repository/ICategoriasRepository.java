package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICategoriasRepository extends JpaRepository<Categoria, Long> {
    Categoria findByNombre(String nombre);
    Categoria findById(long id);
}

