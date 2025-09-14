package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import ar.edu.utn.frba.dds.fuenteDinamica.models.exceptions.ModificacionNoPermitidaException;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Access(AccessType.FIELD)
@Entity
@Table(name = "Hechos")
public class Hecho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Contenido del Hecho
    @Column(nullable = false, name = "titulo")
    private String titulo;

    @Column(nullable = false, name = "descripcion", unique = true)
    private String descripcion;

    @Embedded
    private Categoria categoria; //no la persisto en este sistema pero me interesa guardar su id para facilitar su mapeo

    @Embedded
    private Ubicacion ubicacion;

    @Column(nullable = false, name = "fecha-ocurrencia")
    private LocalDate fechaDeOcurrencia;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "hecho_id", nullable = false) // esto crea/escribe la FK en la tabla del hijo
    private List<ContenidoMultimedia> contenidoMultimedia;

    @Column(nullable = false, name = "fecha-carga")
    private LocalDateTime fechaDeCarga;
    @Column(nullable = false, name = "anonimo")
    private Boolean esAnonimo = Boolean.TRUE; // de base esta en true

    //Metadata
    @Column(name = "fecha-modificacion")
    private LocalDateTime fechaDeModificacion = null; // para verificar los 7 dias

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "nombre", column = @Column(name = "contribuyente_nombre")),
            @AttributeOverride(name = "apellido", column = @Column(name = "contribuyente_apellido"))
    })
    private Contribuyente contribuyente; // el ususario que lo carga

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoHecho estado = EstadoHecho.ACEPTADO; //TODO: Por ahora, por un tema de testing lo dejamos directamente en aceptado pero debería llegar en el input como un pendiente y despues el admin lo acepta.

    @Column(name = "idAdmin")
    private Long idAdmin; //el administrador que gestiono el hecho subido

    @Column(name = "sugerencia")
    private String sugerencia = null;

    public void verificarModificacionValida(Long diasMaximos, LocalDateTime fechaModificacion) {
        if ( getFechaDeCarga().plusDays(diasMaximos).isBefore(fechaModificacion) ) {
            throw new ModificacionNoPermitidaException( String.format("Pasaron los %d dias permitidos para serModificado el hecho", diasMaximos) );
        }
        if ( contribuyente.getId() == null ) { // si no tiene id no esta registrado
            throw new ModificacionNoPermitidaException( "El contribuyente no esta registrado, modificacion no permitida" );
        }
    }

    public void serModificado(Hecho nuevosDatosHecho, Long diasValidosModificacion){
        verificarModificacionValida(diasValidosModificacion, LocalDateTime.now());

        this.setTitulo(nuevosDatosHecho.getTitulo());
        this.setDescripcion(nuevosDatosHecho.getDescripcion());
        this.setCategoria(nuevosDatosHecho.getCategoria());
        this.setUbicacion(nuevosDatosHecho.getUbicacion());
        this.setContenidoMultimedia(nuevosDatosHecho.getContenidoMultimedia());
        this.setFechaDeOcurrencia(nuevosDatosHecho.getFechaDeOcurrencia());
    }

    public void serRevisado(Long idAdmin, EstadoHecho nuevoEstado, String sugerencia) {
        this.setIdAdmin(idAdmin);
        this.setEstado(nuevoEstado);
        if (nuevoEstado == EstadoHecho.SUGERENCIA) {this.sugerencia = sugerencia;}
    }
}
