package ar.edu.utn.frba.dds.fuenteproxy.domain.repositories;


import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.fuentes.*;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IFuentesRepositoryJPA extends JpaRepository<Fuente, Long> {

    List<Fuente> findByHabilitadaTrue();

    void deleteByNombre(String nombre);

    Optional<Fuente> findByNombreAndBaseUrl(String nombre, String baseUrl);

    List<Fuente> findByTipo(TipoFuenteProxy tipoFuente);

    Optional<Fuente> findByTipoAndBaseUrl(TipoFuenteProxy tipoFuente, String baseUrl);

    boolean existsFuenteByBaseUrl(String baseUrl);


    Fuente findByNombre(String nombre);
}
