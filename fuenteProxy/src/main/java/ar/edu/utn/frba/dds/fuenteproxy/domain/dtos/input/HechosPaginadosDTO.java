package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class HechosPaginadosDTO {
    private List<HechoInputDTO> hechos;
    private int page;
    private int totalPages;

    public boolean hayMasPaginas() {
        return page + 1 < totalPages;
    }

}
