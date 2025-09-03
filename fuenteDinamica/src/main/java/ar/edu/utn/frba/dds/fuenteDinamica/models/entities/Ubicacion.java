package ar.edu.utn.frba.dds.fuenteDinamica.models.entities;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ubicacion {
    private String provincia;
    private String localidad;
    private String calle;
    private Integer numero;

    private Double latitud;
    private Double longitud;
}
