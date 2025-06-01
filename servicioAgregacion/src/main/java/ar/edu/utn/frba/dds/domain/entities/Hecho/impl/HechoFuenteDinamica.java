package ar.edu.utn.frba.dds.domain.entities.Hecho.impl;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.HechoBase;
import ar.edu.utn.frba.dds.domain.entities.IContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.domain.entities.Usuario;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class HechoFuenteDinamica implements HechoBase {
    private Long id;
    private Long origenId;
    private Long idFuente;
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;

    private IContenidoMultimedia contenidoMultimedia;
    private Usuario contribuyente;

    @Builder.Default
    private Boolean fueEliminado = false;
}
