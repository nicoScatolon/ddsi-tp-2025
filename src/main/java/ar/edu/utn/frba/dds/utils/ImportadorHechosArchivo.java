package ar.edu.utn.frba.dds.utils;

import ar.edu.utn.frba.dds.Hechos.Hecho;

import java.util.Set;

public interface ImportadorHechosArchivo {

    public Set<Hecho> importarHechosArchivo(String path);
}
