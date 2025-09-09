package ar.edu.utn.frba.dds.domain.dtos.input;

import ar.edu.utn.frba.dds.domain.entities.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoriaDTO;
    private UbicacionInputDTO ubicacion;
    private List<Etiqueta> etiquetas; //posible DTO
    private List<ContenidoMultimedia> contenidoMultimedia; //posible DTO
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private Contribuyente contribuyente; //posible DTO
    private Boolean cargadoAninimamente;
    private Fuente fuente;
}
