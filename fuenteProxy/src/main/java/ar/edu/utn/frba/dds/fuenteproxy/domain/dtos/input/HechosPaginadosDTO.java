package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class HechosPaginadosDTO {
    private List<HechoExternoDTO> hechos;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}
