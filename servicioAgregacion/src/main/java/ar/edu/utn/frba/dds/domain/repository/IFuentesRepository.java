package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;

import java.util.List;

public interface IFuentesRepository {
    List<Fuente> findAll();
    Fuente findById(Long id);
    void agregarFuente(Fuente fuente);
    void eliminarFuente(Long id);
}
