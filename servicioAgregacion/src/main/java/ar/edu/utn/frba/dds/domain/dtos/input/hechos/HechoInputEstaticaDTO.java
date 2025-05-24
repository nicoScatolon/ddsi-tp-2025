package ar.edu.utn.frba.dds.domain.dtos.input.hechos;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HechoInputEstaticaDTO implements IHechoInputDTO{
    private Long id;
    private String titulo;
    private String descripcion;
    private CategoriaInputDTO categoria;
    private UbicacionInputDTO ubicacion;
    private LocalDate fechaDeOcurrencia;
}
