package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;

import java.util.List;

public interface IFuentesRepository {
    List<IFuente> findAll();
    IFuente findById(Long id);
    void saveFuente(IFuente fuente);
    void deleteFuente(Long id);
}
