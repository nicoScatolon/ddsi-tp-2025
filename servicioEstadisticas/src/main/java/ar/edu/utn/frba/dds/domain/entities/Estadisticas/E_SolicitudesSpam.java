package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class E_SolicitudesSpam {
    private Long id;
    //private static String descripcion = "la cantidad de solicitudes que son spam";
    private Integer solicitudesSpam;
    private Integer solicitudesNoSpam;
    private LocalDateTime fechaDeCalculo;
}
