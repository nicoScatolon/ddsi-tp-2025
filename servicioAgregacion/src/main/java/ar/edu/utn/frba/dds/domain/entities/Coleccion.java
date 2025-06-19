package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
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
public class Coleccion {
    @Setter private String handle;
    @Setter private String titulo;
    @Setter private String descripcion;
    @Setter private Boolean actualizarHechos;

    private final List<IFuente> listaFuentes = new ArrayList<>();
    private final Set<ICriterio> listaCriterios = new HashSet<>();
    private List<Hecho> listaHechos = new ArrayList<>();
    private List<Hecho> listaHechosCurados = new ArrayList<>();

    @Builder
    public Coleccion(String titulo, String descripcion) {
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

    public void actualizarHechos() {
        // solamente actualizo los hechos que vengan de fuentes ALMACENADAS, no de las consumidas
        // por eso obtener Hechos Cargados devuelve null si es proxy
        List<Hecho> listaAuxiliar = new ArrayList<>();
        //cargamos todos los hechos de las fuentes
        for (IFuente fuente : listaFuentes) {
            FuenteAdapter adapter = fuente.getTipo().crearAdapter();
            List<Hecho> hechosFuente = adapter.obtenerHechosCargados();
            if (hechosFuente != null) {
                listaAuxiliar.addAll(hechosFuente);
            }
        }
        // filtramos estos hechos
        this.listaHechos = this.filtrarHechos(listaAuxiliar);
        //recordar que proxy no se almacena, asi que al consumir sus hechos hay que filtrarlos
    }

    public List<Hecho> getHechosVisualizar(List<Hecho> hechosConsumidos) {
        // devuelve los hechos listos para verlos por pantalla (filtrados)
        List<Hecho> hechosADevolver = new ArrayList<>();
        hechosADevolver.addAll(listaHechos); // no recalculamos los hechos ya filtrados
        // pero si filtramos los hechos que son consumidos
        hechosADevolver.addAll(this.filtrarHechos(hechosConsumidos));
        return hechosADevolver;
    }


    public List<Hecho> getHechosConFiltro(List<Hecho> hechosConsumidos, ICriterio filtro) {
        List<Hecho> hechosAFiltrar = new ArrayList<>();
        hechosAFiltrar.addAll(listaHechos);
        hechosAFiltrar.addAll(hechosConsumidos);
        return this.filtrarHechos(hechosAFiltrar).stream()
                .filter(filtro::pertenece)
                .toList();
    }

    public List<Hecho> getHechosConFiltro(List<Hecho> hechosConsumidos, Set<ICriterio> filtros) {
        List<Hecho> hechosAFiltrar = new ArrayList<>();
        hechosAFiltrar.addAll(listaHechos);
        hechosAFiltrar.addAll(hechosConsumidos);
        return this.filtrarHechos(hechosAFiltrar).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)))
                .toList();
    }
}
