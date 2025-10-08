package ar.edu.utn.frrba.dds.servicioUsuarios.models.repositories;

import ar.edu.utn.frrba.dds.servicioUsuarios.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
    boolean existsByEmail(String email);
}
