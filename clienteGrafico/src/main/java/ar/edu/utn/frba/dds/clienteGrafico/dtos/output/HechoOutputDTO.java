package ar.edu.utn.frba.dds.clienteGrafico.dtos.output;

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
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaOutputDTO categoria;
    private UbicacionOutputDTO ubicacion;
    private List<EtiquetaOutputDTO> etiquetas; //posible DTO
    private List<ContenidoMultimediaOutputDTO> contenidoMultimedia; //posible DTO
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private ContribuyenteOutputDTO contribuyente; //posible DTO
    private Boolean cargadoAninimamente;
}
