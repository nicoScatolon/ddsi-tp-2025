package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Criterio.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class Coleccion {
    private Set<CriterioInterfaz> listaCriterios;
    @Setter private String titulo;
    @Setter private String descripcion;


    public Coleccion(String titulo) {
        this.listaCriterios = new HashSet<CriterioInterfaz>();
        this.titulo = titulo;
    }

    public void agregarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.add(criterio);
    }

    public void eliminarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.remove(criterio);
    }

    public Set<Hecho> getHechos() {
        // seria NAVEGAR, devuelve la coleccion sin filtros
        
        if(this.listaCriterios.isEmpty()){
            return new HashSet<>();
        }
//        Set<Hecho> listaHechos = fuente.getHechos();
//
//        return listaHechos.stream()
//                .filter(h->this.listaCriterios.stream().allMatch(c -> c.pertenece(h)))
//                .collect(Collectors.toCollection(HashSet::new));
        //ToDO: Cambiar, no existe una fuente, sino que una misma coleccion puede tener hechos de distintas fuentes
        return null;
    }

    public Set<Hecho> getHechosConFiltro(CriterioInterfaz filtro) {
        Set<Hecho> hechosADevolver = this.getHechos();

        if(hechosADevolver.isEmpty()){
            return new HashSet<>();
        }

        return hechosADevolver.stream()
                .filter(filtro::pertenece)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Hecho> getHechosConFiltro(Set<CriterioInterfaz> filtros) {
        Set<Hecho> hechosADevolver = this.getHechos();

        if(hechosADevolver.isEmpty()){
            return new HashSet<>();
        }

        return hechosADevolver.stream()
                .filter(h -> filtros.stream().allMatch( c -> c.pertenece(h)))
                .collect(Collectors.toCollection(HashSet::new));
    }
}