package ar.edu.utn.frba.dds.clienteGrafico.dtos.output.Hechos;

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
public class HechoDinamicaOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaOutputDTO categoria;
    private UbicacionOutputDTO ubicacion;
    private List<ContenidoMultimediaOutputDTO> contenidoMultimedia;
    private LocalDateTime fechaDeOcurrencia;
    private Long contribuyenteId;
    private Boolean cargadoAnonimamente;
}
