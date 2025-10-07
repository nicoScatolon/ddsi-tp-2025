package ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginaHechosResponseDdsDTO {
    @JsonProperty("current_page") private int currentPage;
    private List<DdsHechoInputDTO> data;
    @JsonProperty("first_page_url") private String firstPageUrl;
    private int from;
    @JsonProperty("last_page") private int lastPage;
    @JsonProperty("last_page_url") private String lastPageUrl;
    @JsonProperty("next_page_url") private String nextPageUrl;
    private String path;
    @JsonProperty("per_page") private int perPage;
    @JsonProperty("prev_page_url") private String prevPageUrl;
    private int to;
    private int total;
}
