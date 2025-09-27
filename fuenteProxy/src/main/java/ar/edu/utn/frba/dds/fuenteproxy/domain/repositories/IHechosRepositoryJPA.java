package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories;


import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IHechosRepositoryJPA extends JpaRepository<Hecho, Long> {
    Optional<Hecho> findById_original(String idOriginal);
    Optional<Hecho> findByIdOriginalAndFuente(String idOriginal, Fuente fuente);

    List<String> findAllIdOriginalByFuente(Fuente fuente);

}
