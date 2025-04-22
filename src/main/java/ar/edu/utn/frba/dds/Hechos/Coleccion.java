package ar.edu.utn.frba.dds.Hechos;

import ar.edu.utn.frba.dds.Criterio.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;


@Getter
public class Coleccion {
    private Set<Hecho> listaHechos;
    @Setter private String titulo;
    @Setter private String descripcion;
    private Fuente fuente;
    //private List<Criterio> listaCriterios; //es una forma de saber que criterios tiene establecidos

    public Coleccion(String titulo) {
        this.listaHechos = new HashSet();
        this.titulo = titulo;
    }

    public void setFuente(Fuente fuente){
        this.fuente = fuente;
        this.listaHechos = fuente.getHechos().stream()
                .filter(n -> !n.getFueEliminado())
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void agregarCriterio(CriterioInterfaz criterio) {
        this.listaHechos.removeIf(n -> !criterio.pertenece(n));
        this.filtrarEliminados(); //puede no ser necesario ya que no se navega en este instante
    }

    public void filtrarEliminados(){
        //this.listaHechos.stream().filter(n -> !n.getFueEliminado());
        this.listaHechos.removeIf(n -> n.getFueEliminado());
    }

    public Set<Hecho> navegar() {
        this.filtrarEliminados();
        return this.getListaHechos();
    }

    public Set<Hecho> navegarConFiltro(CriterioInterfaz criterio) {
        this.filtrarEliminados();
        Set<Hecho> hechosADevolver = this.getListaHechos();
        return hechosADevolver.stream()
                .filter(e -> criterio.pertenece(e))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Hecho> navegarConFiltro(Set<CriterioInterfaz> criterio) {
        this.filtrarEliminados();
        Set<Hecho> hechosADevolver = this.getListaHechos();
        return hechosADevolver.stream()
                .filter(h -> criterio.stream().allMatch( c -> c.pertenece(h)))
                .collect(Collectors.toCollection(HashSet::new));
    }
}
