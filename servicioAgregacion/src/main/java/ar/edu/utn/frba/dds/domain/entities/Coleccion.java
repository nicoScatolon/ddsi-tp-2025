package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.TipoFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoComparator.HechoComparator;
import lombok.*;

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

    private final List<IFuente> listaFuentes = new ArrayList<>();
    private final Set<ICriterio> listaCriterios = new HashSet<>();
    private IAlgoritmoConsenso algoritmoConsenso;

    //cada vez que se inicia el sistema los hechos consumidos no van a estar dentro de estas listas porque no se persisten
    private List<Hecho> listaHechos = new ArrayList<>();
    private List<Hecho> listaHechosCurados = new ArrayList<>();

    @Setter private Boolean actualizarHechos;
    @Setter private Boolean curarHechos;

    public Coleccion(String handle, String titulo, String descripcion) {
        this.handle = handle;
        this.titulo = titulo;
        this.descripcion = descripcion;
        if (algoritmoConsenso != null) {
            this.algoritmoConsenso = algoritmoConsenso;
        }
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
        FuenteAdapter adapter = fuente.getTipo().crearAdapter();
        List<Hecho> hechosFuenteEliminada = adapter.obtenerHechos();
        this.listaHechos.removeAll(hechosFuenteEliminada);
        this.listaHechosCurados.removeAll(hechosFuenteEliminada);
        //funciona incluso para proxy porque son el mismo objeto, ambos estan cargados en memoria.
        this.listaFuentes.remove(fuente);
        curarHechos = true;
    }

    public void setAlgoritmoConsenso(IAlgoritmoConsenso algoritmoConsenso) {
        this.algoritmoConsenso = algoritmoConsenso;
        curarHechos = true;
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
            List<Hecho> hechosFuente = adapter.obtenerHechos();
            if (hechosFuente != null) {
                listaAuxiliar.addAll(hechosFuente);
            }
        }
        // filtramos estos hechos
        this.listaHechos = this.filtrarHechos(listaAuxiliar);
        //recordar que proxy no se almacena, asi que al consumir sus hechos hay que filtrarlos
        this.setCurarHechos(true);
    }
    //TODO no sabemos que pasa si arrancamos el programa de 0, que datos de la coleccion salen del repo y cuales se obtienen en el momento
    // porque si los proxy no se guardan asociados a la coleccion, al correr el sistema de 0, no van a haber hechos proxy almacenados en la lista local de hechos

    public void curarHechos() {
        if (algoritmoConsenso == null) {
            this.listaHechosCurados = this.listaHechos;
        }
        else
            this.listaHechosCurados = algoritmoConsenso.curar(listaHechos, listaFuentes);
        setCurarHechos(false);
        //TODO ver que pasa con proxy, que si se actualiza una proxy sola quiza no nos enteramos
    }

    public List<Hecho> getHechos() {
        if (listaHechos.isEmpty()) {this.actualizarHechos();} //esto es asi considerando que no hay un inicio de aplicacion que calcule t0do y lo tenga listo
        return listaHechos.stream().filter(h -> !h.getFueEliminado()).toList();
        //NOTA: actualmente estamos guardando proxy dentro de la lista hechos, entonces estos se actualizan cada 1 hora (segun scheduler)
        // si necesitamos que proxy, al ser consumidas, se actualize mas rapido, habria que hacer peticiones a proxy de sus hechos cada x tiempo / en cada peticion
        // (solo le pedimos sus hechos cargados, no que haga un get, la fuente sabe cuando debe pedirlos nuevamente)
    }

    public List<Hecho> getHechosCurados() {
        if (listaHechosCurados.isEmpty()) { this.curarHechos(); } //esto es asi considerando que no hay un inicio de aplicacion que calcule t0do y lo tenga listo
        return listaHechosCurados.stream().filter(h -> !h.getFueEliminado()).toList();
    }

    public List<Hecho> getHechosConFiltro(List<ICriterio> filtros) {
        return this.filtrarHechos(listaHechos).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)) && !h.getFueEliminado() )
                .toList();
    }

    public List<Hecho> getHechosCuradosYFiltrados(List<ICriterio> filtros){
        return this.filtrarHechos(listaHechosCurados).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)) && !h.getFueEliminado() )
                .toList();
    }
}
