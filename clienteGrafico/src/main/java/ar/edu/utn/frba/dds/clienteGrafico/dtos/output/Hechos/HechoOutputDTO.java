package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.ContribuyenteOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.EtiquetaOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.UbicacionOutputDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private List<EtiquetaOutputDTO> etiquetas;
    private List<ContenidoMultimediaOutputDTO> contenidoMultimedia;
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private ContribuyenteOutputDTO contribuyente;
    private Boolean cargadoAnonimamente;
}
