package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Estadisticas.E_MayorProvinciaPorColeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IE_MayorProvinciaPorColeccionRepository extends JpaRepository<E_MayorProvinciaPorColeccion, Long> {
    List<E_MayorProvinciaPorColeccion> findByColeccion_Handle(String handle);
    void deleteByFechaDeCalculoBefore(LocalDateTime fechaLimite);
}
