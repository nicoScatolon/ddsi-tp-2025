package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.Objects;

public class CompararDescripcion implements IComandComparator{
    @Override
    public boolean comparar(Hecho hecho1, Hecho hecho2) {
        String descripcion1 = normalizarDescripcion(hecho1.getDescripcion());
        String descripcion2 = normalizarDescripcion(hecho2.getDescripcion());
        return descripcion1.compareTo(descripcion2) == 0;
    }

    private String normalizarDescripcion(String descripcion){
        return descripcion
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
