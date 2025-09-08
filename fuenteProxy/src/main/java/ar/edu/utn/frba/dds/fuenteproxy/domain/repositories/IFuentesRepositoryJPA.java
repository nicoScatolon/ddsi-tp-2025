package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFuentesRepositoryJPA extends JpaRepository<Fuente, Long> {

    List<Fuente> findByHabilitadaTrue();

    void deleteByNombre(String nombre);
}
