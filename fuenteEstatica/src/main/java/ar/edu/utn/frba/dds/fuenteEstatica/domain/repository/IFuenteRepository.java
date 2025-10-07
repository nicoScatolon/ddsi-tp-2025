package ar.edu.utn.frba.dds.fuenteEstatica.domain.repository;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.FormatoFuente;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFuenteRepository extends JpaRepository<Fuente, Long> {
    Optional<Fuente> findByTipoAndUri(FormatoFuente tipo, String uri);
}
