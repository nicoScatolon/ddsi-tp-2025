package ar.edu.utn.frba.dds.domain.dtos.output;

import ar.edu.utn.frba.dds.domain.entities.ContenidoMultimedia;
import ar.edu.utn.frba.dds.domain.entities.Contribuyente;
import ar.edu.utn.frba.dds.domain.entities.Etiqueta;
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
public class HechoOutputDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaOutputDTO categoria;
    private UbicacionOutputDTO ubicacion;
    private List<Etiqueta> etiquetas; //posible DTO
    private List<ContenidoMultimedia> contenidoMultimedia; //posible DTO
    private LocalDateTime fechaDeOcurrencia;
    private LocalDateTime fechaDeCarga;
    private Contribuyente contribuyente; //posible DTO
    private Boolean cargadoAninimamente;
    // NOTA los cambios agregados deben ser mostrador por el DTO converter
}

