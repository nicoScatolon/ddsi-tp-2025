package ar.edu.utn.frba.dds.domain.repository;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;

import java.util.List;

public interface IColeccionesRepository {
    Coleccion findByHandle(String handle);
    List<Coleccion> findAll();
    void save(Coleccion coleccion);
    void delete(Coleccion coleccion);
    List<IHecho> hechosByHandle(String handle, List<IHecho> hechosDisponibles);
}
