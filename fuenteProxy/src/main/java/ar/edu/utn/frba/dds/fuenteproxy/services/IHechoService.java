package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoDTO;

import java.util.List;

public interface IHechoService {
    List<HechoDTO> buscarTodos();
}
