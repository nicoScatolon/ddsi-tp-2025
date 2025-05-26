package ar.edu.utn.frba.dds.domain.entities.Hecho.impl;

import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.IHecho;
import ar.edu.utn.frba.dds.domain.entities.IContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class HechoFuenteDinamica implements IHecho {
    private Long id;
    private Long fuenteId;

    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private LocalDate fechaDeOcurrencia;

    private LocalDateTime fechaDeModificacion;
    private IContenidoMultimedia contenidoMultimedia;
    private UsuarioInputDTO contribuyente;

    private Boolean fueEliminado;
}
