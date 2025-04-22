package ar.edu.utn.frba.dds.Hechos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

//Podria ser un Valuable Object
@Setter
@Getter
public class Hecho {
    public Hecho() {
        this.etiquetas = new HashSet<>();
    }

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaDeOcurrencia;
    private HashSet<Etiqueta> etiquetas;


    private LocalDate fechaDeCarga;
    private OrigenHecho origen;
    private Boolean fueEliminado = false;

    public void agregarEtiqueta(Etiqueta etiqueta){
        this.etiquetas.add(etiqueta);
    }
    /*public void agregarEtiqueta(Set<Etiqueta> etiqueta){
        this.etiquetas.addAll(etiqueta);
    }*/
}


// podriamos hacer el que tenga multimedia o no como 2 clases abstractas pero todavia no sabemos