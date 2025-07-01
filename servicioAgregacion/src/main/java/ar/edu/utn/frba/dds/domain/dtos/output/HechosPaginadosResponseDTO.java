package ar.edu.utn.frba.dds.domain.dtos.output;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HechosPaginadosResponseDTO {
    private List<HechoOutputDTO> hechos;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    // Constructor, Getters y Setters
    public HechosPaginadosResponseDTO(List<HechoOutputDTO> hechos, int page, int size, long totalElements) {
        this.hechos = hechos;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }
}
