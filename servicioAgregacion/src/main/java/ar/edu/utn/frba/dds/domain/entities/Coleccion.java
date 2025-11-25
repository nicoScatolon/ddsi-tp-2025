package ar.edu.utn.frba.dds.domain.entities;

import ar.edu.utn.frba.dds.domain.converters.AlgoritmoConcensoConverter;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.IAlgoritmoConsenso;
import ar.edu.utn.frba.dds.domain.entities.Criterio.impl.Criterio;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.adapters.FuenteAdapter;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coleccion_fuente",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "fuente_id")
    )
    private final List<Fuente> listaFuentes = new ArrayList<>();

    @OneToMany(mappedBy = "coleccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Criterio> listaCriterios = new HashSet<>();

    @Convert(converter = AlgoritmoConcensoConverter.class)
    private IAlgoritmoConsenso algoritmoConsenso = null;

    //cada vez que se inicia el sistema los hechos consumidos no van a estar dentro de estas listas porque no se persisten
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coleccion_hechos",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> listaHechos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coleccion_hechosCurados",
            joinColumns = @JoinColumn(name = "coleccion_id"),
            inverseJoinColumns = @JoinColumn(name = "hecho_id")
    )
    private List<Hecho> listaHechosCurados = new ArrayList<>();

    //Ahora que persistimos en BD, no harían falta esas listas en memoria
    @Column(nullable = false, name = "actualizarHechos")
    @Setter private Boolean actualizarHechos = true;

    @Column(nullable = false, name = "curarHechos")
    @Setter private Boolean curarHechos = false; //arranca en false porque curo a partir de la lista de hechos, asi que necesito actualizar primero

    @Column (nullable = false, name= "destacada")
    @Setter private Boolean destacada = false;

    public Coleccion(String handle, String titulo, String descripcion, IAlgoritmoConsenso algoritmoConsenso) {
        this.handle = handle;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.algoritmoConsenso = algoritmoConsenso;
    }

    public void agregarCriterio(Criterio criterio) {
        this.listaCriterios.add(criterio);
        criterio.setColeccion(this);
        actualizarHechos = true;
    }

    public void eliminarCriterio(Criterio criterio) {
        this.listaCriterios.remove(criterio);
        criterio.setColeccion(null);
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
        this.listaHechos = this.listaHechos.stream().filter(h -> !Objects.equals( h.getFuente().getId(), fuente.getId() ) ).toList();
        this.listaHechosCurados = this.listaHechosCurados.stream().filter(h -> !Objects.equals( h.getFuente().getId(), fuente.getId() ) ).toList();
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

        if ( !fuentesEliminadas.isEmpty()) {
            fuentesEliminadas.forEach(this::eliminarFuente);
        }
        if ( !fuentesNuevas.isEmpty() ) {
            fuentesNuevas.forEach(this::agregarFuente);
            curarHechos = false; // quiero que primero se actualize y despues cure, para que no quede mal la lista de hechosCurados
        }

    }

    public void setIAlgoritmoConsenso(IAlgoritmoConsenso IAlgoritmoConsenso) {
        this.algoritmoConsenso = IAlgoritmoConsenso;
        curarHechos = true;
    }

    public List<Hecho> filtrarHechos(List<Hecho> hechosDisponibles) {
        if (this.listaCriterios.isEmpty() || hechosDisponibles.isEmpty()) {
            return hechosDisponibles;
        }
        return hechosDisponibles.stream()
                .filter(h -> this.listaCriterios.stream().allMatch(c -> c.pertenece(h)))
                .collect(Collectors.toList());
    }

    public void actualizarHechos( List<Hecho> hechosFuentes ) {
        // filtramos estos hechos
        this.listaHechos = this.filtrarHechos(hechosFuentes);
        this.setActualizarHechos(false);
        this.setCurarHechos(true);
    }

    public void curarHechos(List<List<Hecho>> listaHechosFuentes) {
        if (algoritmoConsenso == null) {
            this.listaHechosCurados = new ArrayList<>(this.listaHechos);
        } else {
            List<Hecho> resultado = algoritmoConsenso.curar(listaHechos, listaHechosFuentes);
            this.listaHechosCurados = new ArrayList<>(resultado);
        }
        setCurarHechos(false);
    }

    public List<Hecho> getHechos() {
        return listaHechos.stream().filter(h -> !h.getFueEliminado()).toList();
    }

    public List<Hecho> getHechosCurados() {
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
