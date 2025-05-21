package ar.edu.utn.frba.dds.fuenteEstatica.domain.repository;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.Hecho;

import java.util.List;

public interface IImportadorArchivosRepository {
    List<Hecho> findAll();
}
