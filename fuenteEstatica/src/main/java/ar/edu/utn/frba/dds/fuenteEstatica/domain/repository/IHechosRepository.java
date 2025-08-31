package ar.edu.utn.frba.dds.fuenteEstatica.domain.repository;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;

import java.time.LocalDateTime;
import java.util.List;

public interface IHechosRepository {
    List<Hecho> findAll();
    Hecho findById(Long id);
    void save(Hecho hecho);
    void delete(Hecho hecho);
    List<Hecho> findByFechaDeCarga(LocalDateTime fechaDeCarga);
}
