package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class PaginaHechosResponseDTO {
    private int currentPage;
    private List<HechoExternoDTO> data;
    private int lastPage;
}
