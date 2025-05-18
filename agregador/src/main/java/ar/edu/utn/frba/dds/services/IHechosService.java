package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;

import java.util.List;

public interface IHechosService {
    public List<HechoOutputDTO> buscarTodosLosHechos();
}
