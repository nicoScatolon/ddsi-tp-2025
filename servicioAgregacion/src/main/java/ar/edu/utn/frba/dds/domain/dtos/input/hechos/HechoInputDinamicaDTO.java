package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.IContenidoMultimedia;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class HechoInputDinamicaDTO implements IHechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String nombreCategoria;
    private UbicacionInputDTO ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDate fechaDeCarga;

    private LocalDateTime fechaDeModificacion;
    private IContenidoMultimedia contenidoMultimedia;
    private UsuarioInputDTO contribuyente;
}
