package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Criterio.CriterioInterfaz;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
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
    private final Set<CriterioInterfaz> listaCriterios = new HashSet<>();
    @Setter private List<HechoBase> listaHechos;
    @Setter private String handle;
    @Setter private String titulo;
    @Setter private String descripcion;
    @Setter private List<TipoFuente> listaTipoFuentes;
    @Setter private Boolean actualizarHechos;
    //TODO posible cambiar la lista de tipoFuente por una lista de fuentes especificas

    public Coleccion(String handle, String titulo, String descripcion) {
        this.handle = handle;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public void agregarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.add(criterio);
        actualizarHechos = true;
    }

    public void eliminarCriterio(CriterioInterfaz criterio) {
        this.listaCriterios.remove(criterio);
        actualizarHechos = true;
    }

    public List<HechoBase> filtrarHechos(List<HechoBase> hechosDisponibles) {
        if (this.listaCriterios.isEmpty() || hechosDisponibles.isEmpty()) {
            return new ArrayList<>();
        }

        return hechosDisponibles.stream()
                .filter(h -> this.listaCriterios.stream().allMatch(c -> c.pertenece(h)))
                .collect(Collectors.toList());
    }

    public void actualizarHechos(List<HechoBase> hechos) {
        this.listaHechos = this.filtrarHechos(hechos);
    }

    public Set<HechoBase> getHechosConFiltro(List<HechoBase> hechosDisponibles, CriterioInterfaz filtro) {
        return this.filtrarHechos(hechosDisponibles).stream()
                .filter(filtro::pertenece)
                .collect(Collectors.toSet());
    }

    public Set<HechoBase> getHechosConFiltro(List<HechoBase> hechosDisponibles, Set<CriterioInterfaz> filtros) {
        return this.filtrarHechos(hechosDisponibles).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)))
                .collect(Collectors.toSet());
    }
}
