package ar.edu.utn.frba.dds.domain.entities.Hecho;

import ar.edu.utn.frba.dds.domain.entities.*;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Embedded
    private Ubicacion ubicacion;

    @Column (nullable = false, name = "fecha-ocurrencia")
    private LocalDate fechaDeOcurrencia;  //Todo, debería ser localdatetime, modificar en todo el dominio

    @Column (nullable = false, name = "fecha-carga")
    private LocalDateTime fechaDeCarga;

    @OneToMany(mappedBy = "hecho", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContenidoMultimedia> contenidoMultimedia = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name= "origenId")
    private Long origenId; //id que tiene el hecho en su fuente de origen, es un dato mas que lo tenemos para poder decirle a la fuente que paso con su hecho (si se lo elimino)

    @Column (name = "fueEliminado", nullable = false)
    private Boolean fueEliminado = false;

    @Enumerated(EnumType.STRING)
    private TipoHecho tipoHecho;

    @ManyToOne
    private Contribuyente contribuyente = null;

    @Column (name= "cargado-anonimamente")
    private Boolean cargadoAnonimamente = null; //TODO MODIFICAR DINAMICA PARA que tenga esto

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fuente_id")
    private Fuente fuente;

    // metodos

    public void agregarEtiqueta(Etiqueta etiqueta){
        etiquetas.add(etiqueta);
    }

    public void eliminarEtiqueta(Etiqueta etiqueta){
        etiquetas.remove(etiqueta);
    }
}

