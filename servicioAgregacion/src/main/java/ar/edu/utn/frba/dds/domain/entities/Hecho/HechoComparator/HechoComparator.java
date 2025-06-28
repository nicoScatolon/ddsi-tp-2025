package ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator;

import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

public class HechoComparator {
    private static final HechoComparator INSTANCE = new HechoComparator();

    private HechoComparator() { }

    public static HechoComparator getInstance() {
        return INSTANCE;
    }

    public Boolean compararHechos(Hecho h1, Hecho h2) {
        return true;
    }
}
