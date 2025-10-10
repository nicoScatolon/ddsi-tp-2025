package ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.UbicacionInputDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class HechoDinamicaInputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoria;
    private UbicacionInputDTO ubicacion;
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga; //TODO agregar la fecha de modificacion
    private List<ContenidoMultimediaInputDTO> contenidoMultimedia;
    private Long contribuyenteId;
    private EstadoHecho estado;
    private String sugerencia;
    private Boolean cargadoAnonimamente;
}
