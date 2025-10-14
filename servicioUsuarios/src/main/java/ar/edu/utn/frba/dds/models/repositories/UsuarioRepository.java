package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByNombre(String username);

    @Query("""
       select u from Usuario u
       left join fetch u.permisos
       left join fetch u.rol
       where u.nombre = :username
       """)
    Optional<Usuario> findByNombreFetchAuth(@Param("username") String username);

    @Query("""
       select u from Usuario u
       left join fetch u.permisos
       where u.email = :email
       """)
    Optional<Usuario> findByEmailFetchAuth(@Param("email") String email);
}
