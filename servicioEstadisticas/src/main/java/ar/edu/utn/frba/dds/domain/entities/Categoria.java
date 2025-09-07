package ar.edu.utn.frba.dds.domain.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Categoria {
    private String id;
    private String nombre;
    private LocalDateTime fechaActualizacion; //para la base de datos
}
