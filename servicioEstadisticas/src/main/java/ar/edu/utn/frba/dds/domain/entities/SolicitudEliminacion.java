package ar.edu.utn.frba.dds.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SolicitudEliminacion {
    private Long id;
    private Hecho hecho;
    private EstadoDeSolicitud estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaGestion;
    private Boolean eliminada;
}
