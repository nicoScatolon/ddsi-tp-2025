package ar.edu.utn.frba.dds.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ContenidoMultimedia {
    private Long Id;
    private String url;
    private String descripcion;
}
