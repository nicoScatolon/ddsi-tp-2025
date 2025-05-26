package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.repository.IColeccionesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ColeccionesRepository implements IColeccionesRepository {
    private final Map<String, Coleccion> colecciones = new ConcurrentHashMap<>();

    @Override
    public Coleccion findByHandle(String handle) {
        return colecciones.get(handle);
    }

    @Override
    public List<Coleccion> findAll() {
        return new ArrayList<>(colecciones.values());
    }

    @Override
    public void save(Coleccion coleccion) {
        colecciones.put(coleccion.getHandle(), coleccion);
    }

    @Override
    public void delete(Coleccion coleccion) {
        colecciones.remove(coleccion.getHandle());
    }

    @Override
    public List<IHecho> hechosByHandle(String handle, List<IHecho> hechosDisponibles) {
        Coleccion coleccion = colecciones.get(handle);
        if (coleccion == null) return List.of();
        return new ArrayList<>(coleccion.filtrarHechos(hechosDisponibles));
    }
}
