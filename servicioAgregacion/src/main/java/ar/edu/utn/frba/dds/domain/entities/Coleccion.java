package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class Coleccion {
    @Builder.Default private List<IFuente> listaFuentes = new ArrayList<>();
    @Builder.Default  private List<Hecho> listaHechos = new ArrayList<>();
    @Builder.Default  private final Set<ICriterio> listaCriterios = new HashSet<>();
    @Setter private String handle;
    @Setter private String titulo;
    @Setter private String descripcion;

    @Setter private Boolean actualizarHechos;

    public Coleccion(String handle, String titulo, String descripcion) {
        this.handle = handle;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public void agregarCriterio(ICriterio criterio) {
        this.listaCriterios.add(criterio);
        actualizarHechos = true;
    }

    public void eliminarCriterio(ICriterio criterio) {
        this.listaCriterios.remove(criterio);
        actualizarHechos = true;
    }

    public void agregarFuente(IFuente fuente) {
        this.listaFuentes.add(fuente);
        actualizarHechos = true;
    }

    public void eliminarFuente(IFuente fuente) {
        this.listaFuentes.remove(fuente);
        actualizarHechos = true;
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechosDisponibles) {
        if (this.listaCriterios.isEmpty() || hechosDisponibles.isEmpty()) {
            return new ArrayList<>();
        }

        return hechosDisponibles.stream()
                .filter(h -> this.listaCriterios.stream().allMatch(c -> c.pertenece(h)))
                .collect(Collectors.toList());
    }

    public void actualizarHechos(List<Hecho> hechos) {
        this.listaHechos = this.filtrarHechos(hechos);
    }

    public Set<Hecho> getHechosConFiltro(List<Hecho> hechosDisponibles, ICriterio filtro) {
        return this.filtrarHechos(hechosDisponibles).stream()
                .filter(filtro::pertenece)
                .collect(Collectors.toSet());
    }

    public Set<Hecho> getHechosConFiltro(List<Hecho> hechosDisponibles, Set<ICriterio> filtros) {
        return this.filtrarHechos(hechosDisponibles).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)))
                .collect(Collectors.toSet());
    }
}
