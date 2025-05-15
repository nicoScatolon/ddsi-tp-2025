package ar.edu.utn.frba.dds.domain.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Hecho {
    private long id;

    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;

    private LocalDate fechaDeCarga;
    private Boolean fueEliminado = false;

    private Usuario contribuyente;
}