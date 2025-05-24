package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

import java.util.List;
import java.util.Optional;

public interface IHechosRepository {
    List<IHecho> findAll();
    IHecho findById(Long id);
    Optional<IHecho> findByFuenteID(Long fuenteID, Class<? extends IHecho> claseEsperada);
    void saveAll(List<IHecho> hechos);
    void delete(IHecho hecho);
}
