package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;

import java.util.List;
import java.util.Optional;

public interface IHechosRepository {
    List<HechoBase> findAll();
    HechoBase findById(Long id);
    Optional<HechoBase> findByFuenteID(Long fuenteID, Class<? extends HechoBase> claseEsperada);
    void saveAll(List<HechoBase> hechos);
    void delete(HechoBase hecho);
}
