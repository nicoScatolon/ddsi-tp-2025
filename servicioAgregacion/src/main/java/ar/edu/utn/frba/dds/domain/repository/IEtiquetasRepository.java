package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Etiqueta;

import java.util.List;

public interface IEtiquetasRepository {
    List<Etiqueta> findAll();
    Etiqueta findByID(Long id);
    Etiqueta save(Etiqueta etiqueta);
    void delete(Long id);
}
