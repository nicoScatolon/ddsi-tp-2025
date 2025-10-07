package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Embeddable
public class Contribuyente {
    @Transient
    private Long id;
    // consideramos que el id nos viene de el sistema de autenticacion de sesiones

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido", nullable = false)
    private String apellido;
    // si se carga un hecho de forma anonima, no se va a poder cargar, 
}
