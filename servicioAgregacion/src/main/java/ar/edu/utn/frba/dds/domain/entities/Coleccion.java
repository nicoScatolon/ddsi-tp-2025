package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Criterio.*;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.repository.impl.HechosRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Getter
public class Coleccion {
    private final Set<CriterioInterfaz> listaCriterios;
    @Setter private String handle;
    @Setter private String titulo;
    @Setter private String descripcion;
    @Setter private HechosRepository hechosRepository;


    public Coleccion(String titulo, String descripcion, HechosRepository hechosRepository) {
        this.listaCriterios = new HashSet<CriterioInterfaz>();
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.hechosRepository = hechosRepository;
    }

    public void agregarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.add(criterio);
    }

    public void eliminarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.remove(criterio);
    }

    public Set<IHecho> getHechos() {
        
        if(this.listaCriterios.isEmpty()){
            return new HashSet<>();
        }

        List<IHecho> listaHechos = hechosRepository.findAll();
        return listaHechos.stream()
                .filter(h->this.listaCriterios.stream().allMatch(c -> c.pertenece(h)))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<IHecho> getHechosConFiltro(CriterioInterfaz filtro) {
        Set<IHecho> hechosADevolver = this.getHechos();

        if(hechosADevolver.isEmpty()){
            return new HashSet<>();
        }

        return hechosADevolver.stream()
                .filter(filtro::pertenece)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<IHecho> getHechosConFiltro(Set<CriterioInterfaz> filtros) {
        Set<IHecho> hechosADevolver = this.getHechos();

        if(hechosADevolver.isEmpty()){
            return new HashSet<>();
        }

        return hechosADevolver.stream()
                .filter(h -> filtros.stream().allMatch( c -> c.pertenece(h)))
                .collect(Collectors.toCollection(HashSet::new));
    }
}