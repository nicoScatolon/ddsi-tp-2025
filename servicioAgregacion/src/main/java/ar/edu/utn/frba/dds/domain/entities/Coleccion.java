package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.AlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Criterio.ICriterio;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.Criterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "coleccion")
public class Coleccion {
    @Id
    @Setter private String handle;

    @Column(nullable = false, name = "titulo")
    @Setter private String titulo;
    @Column(nullable = false, columnDefinition = "TEXT", name = "descripcion")
    @Setter private String descripcion;

    @ManyToMany
    @JoinTable(
            name = "coleccion_fuente",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id")
    )
    private final List<Fuente> listaFuentes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "coleccion_criterios",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "criterio_id")
    )
    private final Set<Criterio> listaCriterios = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "algoritmoConsenso_id")
    private AlgoritmoConsenso algoritmoConsenso = null;

    //cada vez que se inicia el sistema los hechos consumidos no van a estar dentro de estas listas porque no se persisten
    @Transient
    private List<Hecho> listaHechos = new ArrayList<>();
    @Transient
    private List<Hecho> listaHechosCurados = new ArrayList<>();

    //Ahora que persistimos en BD, no harían falta esas listas en memoria

    @Setter private Boolean actualizarHechos = true;
    @Setter private Boolean curarHechos = false; //arranca en false porque curo a partir de la lista de hechos, asi que necesito actualizar primero

    public Coleccion(String handle, String titulo, String descripcion, AlgoritmoConsenso algoritmoConsenso) {
        this.handle = handle;
        this.titulo = titulo;
        this.descripcion = descripcion;
        if (algoritmoConsenso != null) {
            this.algoritmoConsenso = algoritmoConsenso;
        }
    }

    public void agregarCriterio(Criterio criterio) {
        this.listaCriterios.add(criterio);
        actualizarHechos = true;
    }

    public void eliminarCriterio(Criterio criterio) {
        this.listaCriterios.remove(criterio);
        actualizarHechos = true;
    }

    public void setListaCriterios(Set<Criterio> criterios){
        this.listaCriterios.clear();
        this.listaCriterios.addAll(criterios);
    }

    public void agregarFuente(Fuente fuente) {
        this.listaFuentes.add(fuente);
        actualizarHechos = true;
    }

    public void eliminarFuente(Fuente fuente) {
        FuenteAdapter adapter = fuente.getTipo().crearAdapter(fuente);
        List<Hecho> hechosFuenteEliminada = adapter.obtenerHechos();
        this.listaHechos.removeAll(hechosFuenteEliminada);
        this.listaHechosCurados.removeAll(hechosFuenteEliminada);
        //funciona incluso para proxy porque son el mismo objeto, ambos estan cargados en memoria.
        this.listaFuentes.remove(fuente);
        curarHechos = true;
    }

    public void setListaFuentes(List<Fuente> nuevasFuentes) {
        //con las fuentes comunes no hago nada
        List<Fuente> fuentesNuevas = nuevasFuentes.stream()
                .filter(f2 -> !listaFuentes.contains(f2))
                .toList();

        List<Fuente> fuentesEliminadas = listaFuentes.stream()
                .filter(f1 -> !nuevasFuentes.contains(f1))
                .toList();
        if ( !fuentesNuevas.isEmpty() ) {
            fuentesNuevas.forEach(this::agregarFuente);
            curarHechos = false; // quiero que primero se actualize y despues cure, para que no quede mal la lista de hechosCurados
        }
        if ( !fuentesEliminadas.isEmpty()) {
            fuentesEliminadas.forEach(this::eliminarFuente);
        }
    }

    public void setAlgoritmoConsenso(AlgoritmoConsenso algoritmoConsenso) {
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
        for (Fuente fuente : listaFuentes) {
            FuenteAdapter adapter = fuente.getTipo().crearAdapter(fuente);
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
    // no sabemos que pasa si arrancamos el programa de 0, que datos de la coleccion salen del repo y cuales se obtienen en el momento
    // porque si los proxy no se guardan asociados a la coleccion, al correr el sistema de 0, no van a haber hechos proxy almacenados en la lista local de hechos

    public void curarHechos() {
        if (algoritmoConsenso == null) {
            this.listaHechosCurados = this.listaHechos;
        }
        else
            this.listaHechosCurados = algoritmoConsenso.curar(listaHechos, listaFuentes);
        setCurarHechos(false);
    }
    //ver que pasa con proxy, que si se actualiza una proxy sola quiza no nos enteramos

    public List<Hecho> getHechos() {
        //if (listaHechos.isEmpty()) {this.actualizarHechos();} //esto es asi considerando que no hay un inicio de aplicacion que calcule t0do y lo tenga listo
        return listaHechos.stream().filter(h -> !h.getFueEliminado()).toList();
    }
    //NOTA: actualmente estamos guardando proxy dentro de la lista hechos, entonces estos se actualizan cada 1 hora (segun scheduler)
    // si necesitamos que proxy, al ser consumidas, se actualize mas rapido, habria que hacer peticiones a proxy de sus hechos cada x tiempo / en cada peticion
    // (solo le pedimos sus hechos cargados, no que haga un get, la fuente sabe cuando debe pedirlos nuevamente)

    public List<Hecho> getHechosCurados() {
        //if (listaHechosCurados.isEmpty()) { this.curarHechos(); } //esto es asi considerando que no hay un inicio de aplicacion que calcule t0do y lo tenga listo
        return listaHechosCurados.stream().filter(h -> !h.getFueEliminado()).toList();
    }

    public List<Hecho> getHechosConFiltro(List<Criterio> filtros) {
        return this.filtrarHechos(listaHechos).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)) && !h.getFueEliminado() )
                .toList();
    }

    public List<Hecho> getHechosCuradosYFiltrados(List<Criterio> filtros){
        return this.filtrarHechos(listaHechosCurados).stream()
                .filter(h -> filtros.stream().allMatch(f -> f.pertenece(h)) && !h.getFueEliminado() )
                .toList();
    }
}
