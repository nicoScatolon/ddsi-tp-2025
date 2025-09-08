package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IColeccionesRepository extends JpaRepository<Coleccion, Long> {
    Coleccion findByHandle(String handle);
}
