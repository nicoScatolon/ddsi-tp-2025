package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IEtiquetasRepository extends JpaRepository<Etiqueta, Long> {

}
