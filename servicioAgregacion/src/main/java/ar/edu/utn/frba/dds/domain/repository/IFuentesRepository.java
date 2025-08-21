package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface IFuentesRepository {
    List<IFuente> findAll();
    IFuente findById(Long id);
    List<IFuente> findAllById(Set<Long> ids);
    void saveFuente(IFuente fuente);
    void deleteFuente(Long id);
}
