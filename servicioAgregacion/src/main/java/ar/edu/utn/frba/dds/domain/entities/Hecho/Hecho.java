package ar.edu.utn.frba.dds.domain.entities.Hecho;

import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "hechos")
public class Hecho {
    @Column (nullable = false, name = "titulo")
    private String titulo;

    @Column(columnDefinition = "TEXT", unique = true, name = "descripcion")
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToMany
    @JoinTable(
            name = "hecho_etiqueta",
            joinColumns = @JoinColumn(name = "hecho_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id"))
    private List<Etiqueta> etiquetas = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    @Column (nullable = false, name = "fecha-ocurrencia")
    private LocalDateTime fechaDeOcurrencia;

    @Column (nullable = false, name = "fecha-carga")
    private LocalDateTime fechaDeCarga;

    @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContenidoMultimedia> contenidoMultimedia = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name= "origenId")
    private Long origenId; //id que tiene el hecho en su fuente de origen, es un dato mas que lo tenemos para poder decirle a la fuente que paso con su hecho (si se lo elimino)

    @Builder.Default
    @Column (name = "fueEliminado", nullable = false)
    private Boolean fueEliminado = false;

    @Enumerated(EnumType.STRING)
    private TipoHecho tipoHecho;

    @Builder.Default
    @Column (name = "contribuyenteId")
    private Long contribuyenteId = null; //el id del contribuyente en la base de datos del servicio de usuarios

    @Builder.Default
    @Column (name= "cargado-anonimamente")
    private Boolean cargadoAnonimamente = null;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    @Builder.Default
    @Column (name= "destacado")
    private Boolean destacado = false; // empieza siempre como false

    // metodos

    public void agregarEtiqueta(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }

    public void eliminarEtiqueta(Etiqueta etiqueta){
        etiquetas.remove(etiqueta);
    }

    public void actualizarse(Hecho hechoNuevo) {
        if (!Objects.equals(hechoNuevo.getOrigenId(), this.origenId)) { throw new RuntimeException("no somos el mismo hecho"); }
        titulo = hechoNuevo.getTitulo();
        descripcion = hechoNuevo.getDescripcion();
        categoria = hechoNuevo.getCategoria();
        fechaDeOcurrencia = hechoNuevo.getFechaDeOcurrencia();
        fechaDeCarga = hechoNuevo.getFechaDeCarga();
        contenidoMultimedia = hechoNuevo.getContenidoMultimedia();
    }
}

