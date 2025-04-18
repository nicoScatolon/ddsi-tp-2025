package ar.edu.utn.frba.dds.Hechos;

import ar.edu.utn.frba.dds.Criterio.Criterio;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.stream.Collectors;

@Getter
@Setter
public class Coleccion {
    private HashSet<Hecho> listaHechos;
    private String titulo;
    private String descripcion;
    private Fuente fuente;
    private Criterio criterio;

    public Coleccion(Fuente fuente,Criterio criterio) {
        this.listaHechos = new HashSet();
        this.fuente = fuente;
        this.criterio = criterio;
        this.filtrarHechos();
    }

    public void cambiarCriterio(Criterio criterio) {
        this.criterio = criterio;
        this.filtrarHechos();
    }

    public void filtrarHechos() {
        this.listaHechos = fuente.getHechos().stream()
                .filter(n -> criterio.pertenece(n))
                .collect(Collectors.toCollection(HashSet::new));
    }
}
