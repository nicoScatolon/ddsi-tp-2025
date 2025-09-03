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

@Entity
@Table(name = "Hechos")
public class Hecho {
    //Contenido del Hecho
    @Column(nullable = false, name = "titulo")
    private String titulo;
    @Column(nullable = false, name = "descripcion")
    private String descripcion;
    //TODO
    private Categoria categoria; //no la persisto en este sistema pero me interesa guardar su id para facilitar su mapeo
    //TODO
    private Ubicacion ubicacion;
    @Column(nullable = false, name = "fecha-ocurrencia")
    private LocalDate fechaDeOcurrencia;
    //TODO
    private List<ContenidoMultimedia> contenidoMultimedia;
    @Column(nullable = false, name = "fecha-carga")
    private LocalDateTime fechaDeCarga;
    @Column(nullable = false, name = "anonimo")
    private Boolean esAnonimo = Boolean.TRUE; // de base esta en true

    //Metadata
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "fecha-modificacion")
    private LocalDateTime fechaDeModificacion = null; // para verificar los 7 dias
    //TODO
    private Contribuyente contribuyente; // el ususario que lo carga
    //TODO
    private EstadoHecho estado = EstadoHecho.PENDIENTE; // para la respuesta del administrador
    //TODO
    private Long idAdmin; //el administrador que gestiono el hecho subido
    //TODO
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
