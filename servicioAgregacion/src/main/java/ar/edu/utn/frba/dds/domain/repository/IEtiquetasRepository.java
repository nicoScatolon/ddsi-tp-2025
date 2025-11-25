package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IEtiquetasRepository extends JpaRepository<Etiqueta, Long> {
    @Query("SELECT e.nombre FROM Etiqueta e")
    List<String> findAllNombres();

    List<Etiqueta> findByNombre(String nombre);
}
