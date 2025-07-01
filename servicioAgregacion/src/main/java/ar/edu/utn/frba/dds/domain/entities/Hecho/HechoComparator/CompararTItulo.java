package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

public class CompararTItulo implements IComandComparator{
    public boolean comparar(Hecho hecho1, Hecho hecho2){
        String titulo1 = normalizarTitulo(hecho1.getTitulo());
        String titulo2 = normalizarTitulo(hecho2.getTitulo());
        return titulo1.compareTo(titulo2) == 0;
    }

    private String normalizarTitulo(String titulo){
        return titulo
                .trim()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
