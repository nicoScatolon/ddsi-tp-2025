package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
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
        if (coleccion.getHandle() == null){
            coleccion.setHandle(this.stringToHandle(coleccion.getTitulo()));
        }
        colecciones.put(coleccion.getHandle(), coleccion);
    }

    @Override
    public void delete(Coleccion coleccion) {
        colecciones.remove(coleccion.getHandle());
    }


    private String stringToHandle(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("El string no puede ser nulo ni vacío.");
        }

        return string
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
    }

}
