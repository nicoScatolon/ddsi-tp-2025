package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import ar.edu.utn.frba.dds.fuenteDinamica.models.exceptions.ModificacionNoPermitidaException;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hecho {
    //Contenido del Hecho
    private String titulo;
    private String descripcion;
    private String categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private IContenidoMultimedia contenidoMultimedia;
    private LocalDateTime fechaDeCarga;

    //Metadata
    private Long id;
    private LocalDateTime fechaDeModificacion = null; // para verificar los 7 dias
    private Contribuyente contribuyente; // el ususario que lo carga
    private EstadoHecho estado = EstadoHecho.PENDIENTE; // para la respuesta del administrador
    private Long idAdmin; //el administrador que gestiono el hecho subido
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
