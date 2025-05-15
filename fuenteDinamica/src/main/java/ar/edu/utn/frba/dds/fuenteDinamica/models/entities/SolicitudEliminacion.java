package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class SolicitudEliminacion {
    private Long id;
    private Hecho hecho;
    private Usuario creador;
    private Usuario adminnistrador;
    private LocalDate fechaCreacion;
    private LocalDate fechaGestion;
}
