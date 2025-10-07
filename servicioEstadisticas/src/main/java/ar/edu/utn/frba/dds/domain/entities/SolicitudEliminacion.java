package ar.edu.utn.frba.dds.domain.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SolicitudEliminacion {
    private Long id;
    //private Hecho hecho; - por ahora no es necesario
    private EstadoDeSolicitud estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaGestion;
    //private Boolean eliminada;
}
