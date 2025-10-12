package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IColeccionesRepository extends JpaRepository<Coleccion, Long> {
    Coleccion findByHandle(String handle);
    Optional<Coleccion> findById(Long aLong);
    boolean existsColeccionByHandle(String handle);

    List<Coleccion> findAllByDestacada(boolean esDestacada);

}
