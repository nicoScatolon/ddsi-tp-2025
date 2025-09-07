package ar.edu.utn.frba.dds.fuenteEstatica.domain.repository;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IHechosRepository extends JpaRepository<Hecho, Long> {
    List<Hecho> findByFechaDeCargaAfter(LocalDateTime fechaCarga); //Ahi lo mapea automatico, sino le pongo query manualmente con @Query
}
