package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class Coleccion {
    private final Set<CriterioInterfaz> listaCriterios = new HashSet<>();
    @Setter private String handle;
    @Setter private String titulo;
    @Setter private String descripcion;

    public Coleccion(String handle, String titulo, String descripcion) {
        this.handle = handle;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public void agregarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.add(criterio);
    }

    public void eliminarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.remove(criterio);
    }

    public Set<IHecho> filtrarHechos(List<IHecho> hechosDisponibles) {
        if (this.listaCriterios.isEmpty() || hechosDisponibles.isEmpty()) {
            return new HashSet<>();
        }

        return hechosDisponibles.stream()
                .filter(h -> this.listaCriterios.stream().allMatch(c -> c.pertenece(h)))
                .collect(Collectors.toSet());
    }

    public Set<IHecho> getHechosConFiltro(List<IHecho> hechosDisponibles, CriterioInterfaz filtro) {
        return this.filtrarHechos(hechosDisponibles).stream()
                .filter(filtro::pertenece)
                .collect(Collectors.toSet());
    }

    public Set<IHecho> getHechosConFiltro(List<IHecho> hechosDisponibles, Set<CriterioInterfaz> filtros) {
        return this.filtrarHechos(hechosDisponibles).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)))
                .collect(Collectors.toSet());
    }
}
