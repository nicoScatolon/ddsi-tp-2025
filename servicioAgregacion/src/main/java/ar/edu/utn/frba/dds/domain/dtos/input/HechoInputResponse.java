package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HechoInputResponse {
    private List<HechoInputDTO> hechoInputDTOs;


}
