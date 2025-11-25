package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.ContenidoMultimediaInputDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionPreviewInputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    private AlgoritmoConsensoDTO algoritmoCurado;
    private List<FuenteInputDTO> fuentes;
    private Boolean destacada;
    private ContenidoMultimediaInputDTO contenidoMultimedia;
}
