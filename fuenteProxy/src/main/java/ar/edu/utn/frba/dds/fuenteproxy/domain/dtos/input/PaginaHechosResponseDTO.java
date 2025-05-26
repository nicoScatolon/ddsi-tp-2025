package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginaHechosResponseDTO {
    private int currentPage;
    private List<HechoExternoDTO> data;
    private int lastPage;
}
