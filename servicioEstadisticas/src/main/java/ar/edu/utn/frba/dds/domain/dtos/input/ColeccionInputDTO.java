package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class ColeccionInputDTO {
    private String handle;
    private String titulo;
    private String descripcion;
    //el algoritmo de consenso no me interesa
    private List<HechoInputDTO> hechos;
}
