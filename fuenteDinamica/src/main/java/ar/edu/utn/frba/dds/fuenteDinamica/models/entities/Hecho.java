package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

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
    // private String sugerencia = null;

    private Boolean actualizar = true;
}
