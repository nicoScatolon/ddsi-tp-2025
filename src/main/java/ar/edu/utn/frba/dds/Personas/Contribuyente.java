package ar.edu.utn.frba.dds.Personas;

import ar.edu.utn.frba.dds.Hechos.Hecho;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Contribuyente extends Visualizador {
    private String nombre;
    private String apellido;
    private int edad;
    private boolean anonimato;

    public Contribuyente(String nombre, String apellido) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.anonimato = true;
    }

    public void cargarHecho(){}
    public void solicitarEliminacionHecho(Hecho hecho){}
}
