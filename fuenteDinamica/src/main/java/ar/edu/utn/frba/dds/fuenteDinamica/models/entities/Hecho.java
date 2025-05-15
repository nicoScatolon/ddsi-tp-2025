package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
public class Hecho {
    private Long id;

    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private IContenidoMultimedia contenidoMultimedia;
    private LocalDate fechaDeCarga;

    private Boolean fueEliminado = false; // para las solicitudes

    private Boolean puedeModificar;
    private LocalDate fechaDeModificacion; // para verificar los 7 dias

    private Usuario contribuyente; // el ususario que lo carga

}
