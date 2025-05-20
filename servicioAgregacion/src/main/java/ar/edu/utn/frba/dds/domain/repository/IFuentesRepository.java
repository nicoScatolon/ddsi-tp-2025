package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface IFuentesRepository {
    List<Fuente> obtenerFuentes();
}
