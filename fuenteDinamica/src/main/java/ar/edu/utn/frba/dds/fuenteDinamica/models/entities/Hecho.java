package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class Hecho {
    private Long id;

    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private IContenidoMultimedia contenidoMultimedia;
    private LocalDateTime fechaDeCarga;

    private Boolean fueEliminado = false; // para las solicitudes

    private Boolean puedeModificar;
    private LocalDateTime fechaDeModificacion; // para verificar los 7 dias
    private Usuario contribuyente; // el ususario que lo carga

    private EstadoHecho estado; // para la respuesta del administrador

    private Boolean actualizar;
    //para no tener que actualizar los datos de hecho, considero que la anonimidad del usuario se puede cambiar, y por ende se debe verificar al usuario y no al hecho para ver si se muestra o no

}
