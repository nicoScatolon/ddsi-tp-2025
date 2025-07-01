package ar.edu.utn.frba.dds.domain.repository.impl;

import ar.edu.utn.frba.dds.domain.entities.Coleccion;
import ar.edu.utn.frba.dds.domain.repository.IColeccionesRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ColeccionesRepository implements IColeccionesRepository {
    private final Map<String, Coleccion> colecciones = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

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

        String handle =  string.trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")  // reemplaza caracteres no alfanuméricos por guiones
                .replaceAll("^-+|-+$", "");     // quita guiones al inicio o final
        handle = handle + "-" + idGenerator.getAndIncrement();

        return handle;
    }

}

//handle = titulo + contador
// ej1: "111" + "-" + 1 -> 111-1
// ej2: "111" + 2 -> 111-2
// ej11: "11" + 11 -> 11-11
