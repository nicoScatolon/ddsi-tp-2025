package ar.edu.utn.frba.dds.fuenteEstatica.domain.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hecho {
    private Long id;

    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private Categoria categoria;
    private LocalDate fechaDeOcurrencia;

    private LocalDateTime fechaDeCarga;
    private Boolean fueEliminado = false;

    private Usuario contribuyente;
}
