package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Contribuyente {
    private Long id;
    // consideramos que el id nos viene de el sistema de autenticacion de sesiones
    private String nombre;
    private String apellido;
    // si se carga un hecho de forma anonima, no se va a poder cargar, 
}
