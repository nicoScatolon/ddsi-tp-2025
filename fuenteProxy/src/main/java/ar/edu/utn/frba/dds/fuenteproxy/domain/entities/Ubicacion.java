package ar.edu.utn.frba.dds.fuenteproxy.domain.entities;

import lombok.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ubicacion {
    private Double latitud;
    private Double longitud;


}


