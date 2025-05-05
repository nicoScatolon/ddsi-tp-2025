package ar.edu.utn.frba.dds.Hechos;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Fuente {
    private Set<Hecho> hechos;

    public Fuente() {
        this.hechos = new HashSet<>();
    }
}

//ToDO filtrar hechos eliminados antes de devolver la lista
