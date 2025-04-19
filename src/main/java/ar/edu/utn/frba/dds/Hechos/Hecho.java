package ar.edu.utn.frba.dds.Hechos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//Podria ser un Valuable Object
@Setter
@Getter
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Double latitud;
    private Double longitud;
    private LocalDate fechaDeOcurrencia;


    private LocalDate fechaDeCarga;
    private OrigenHecho origen;
    private Boolean fueEliminado = false;

}


// podriamos hacer el que tenga multimedia o no como 2 clases abstractas pero todavia no sabemos