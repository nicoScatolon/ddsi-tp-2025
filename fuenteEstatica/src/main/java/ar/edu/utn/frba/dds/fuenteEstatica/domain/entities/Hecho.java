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
    //Datos
    private String titulo;
    private String descripcion;
    private Ubicacion ubicacion;
    private String categoria;
    private LocalDate fechaDeOcurrencia;

    //Metadata
    private Long id;
    private LocalDateTime fechaDeCarga;
    private Boolean actualizar = true; //le dice al service cuando si debe enviar o no el hecho
    //private Boolean fueEliminado = false; -> No sabemos si se modifica el hecho solamente en el agregador o aca tambien
}
