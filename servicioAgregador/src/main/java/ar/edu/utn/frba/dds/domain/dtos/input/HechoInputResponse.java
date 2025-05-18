package ar.edu.utn.frba.dds.domain.dtos.input;

import lombok.Data;

import java.util.List;

@Data
public class HechoInputResponse {
    private List<HechoInputDTO> hechoInputDTOs;
}
