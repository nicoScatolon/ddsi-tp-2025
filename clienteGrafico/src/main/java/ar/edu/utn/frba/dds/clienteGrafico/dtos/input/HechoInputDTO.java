package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoria;
    private UbicacionInputDTO ubicacion;
    private List<EtiquetaInputDTO> etiquetas; //posible DTO
    private List<ContenidoMultimediaInputDTO> contenidoMultimedia; //posible DTO
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private ContribuyenteInputDTO contribuyente; //posible DTO
    private Boolean cargadoAninimamente;
}
