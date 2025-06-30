package ar.edu.utn.frba.dds.domain.dtos.output;

import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate fechaDeOcurrencia;
}

