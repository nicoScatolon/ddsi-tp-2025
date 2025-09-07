package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "SolicitudesSpam")
public class E_SolicitudesSpam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private static String descripcion = "la cantidad de solicitudes que son spam";
    @Column(name = "cant-solicitudes-spam")
    private Integer solicitudesSpam;
    @Column(name = "cant-solicitudes-no-spam")
    private Integer solicitudesNoSpam;
    @Column(name = "fecha-calculo")
    private LocalDateTime fechaDeCalculo;
}
