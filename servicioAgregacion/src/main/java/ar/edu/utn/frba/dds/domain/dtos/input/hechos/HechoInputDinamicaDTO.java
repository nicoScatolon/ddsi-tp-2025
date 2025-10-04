package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.IContenidoMultimedia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HechoInputDinamicaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private UbicacionInputDTO ubicacion;
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;

    private List<ContenidoMultimedia> contenidoMultimedia;
    private UsuarioInputDTO contribuyente;
}
