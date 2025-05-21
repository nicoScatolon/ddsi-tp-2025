package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.UbicacionDTO;

import java.util.List;

public interface IUbicacionService {
    List<UbicacionDTO> buscarTodos();
}
