package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoria;
    private UbicacionInputDTO ubicacion;
    private List<EtiquetaInputDTO> etiquetas; //posible DTO
    private List<ContenidoMultimediaInputDTO> contenidoMultimedia; //posible DTO
    private LocalDate fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private ContribuyenteInputDTO contribuyente; //posible DTO
    private Boolean cargadoAninimamente;
}
