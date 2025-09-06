package ar.edu.utn.frba.dds.domain.entities.Estadisticas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class E_SolicitudesSpam {
    private Long id;
    //private static String descripcion = "la cantidad de solicitudes que son spam";
    private Integer solicitudesSpam;
    private Integer solicitudesNoSpam;
    private LocalDateTime fechaCalculo;
}
