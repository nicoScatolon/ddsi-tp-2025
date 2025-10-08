package ar.edu.utn.frba.dds.clienteGrafico.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechoInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoria;
    private UbicacionInputDTO ubicacion;
    private LocalDateTime fechaDeOcurrencia;
    private List<EtiquetaInputDTO> etiquetas;
    private List<ContenidoMultimediaInputDTO> contenidoMultimedia;
    private LocalDateTime fechaDeCarga;
    private ContribuyenteInputDTO contribuyente;
    private Boolean cargadoAnonimamente;
    private FuenteInputDTO fuente;
}
