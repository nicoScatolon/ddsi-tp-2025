package ar.edu.utn.frba.dds.domain.entities.Hecho;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Contribuyente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.domain.entities.IContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;

    private IContenidoMultimedia contenidoMultimedia;

    private Long id;
    private Long origenId;
    private IFuente fuente;

    private Boolean fueEliminado = false;

    private TipoHecho tipoHecho;
    private Contribuyente contribuyente = null;
}

// origen: Contribuyente / archivo / fuente externa -> el segundo sera un nombre
// no puedo tener distinto comportamiento entre hechos -> mucho acoplamiento




