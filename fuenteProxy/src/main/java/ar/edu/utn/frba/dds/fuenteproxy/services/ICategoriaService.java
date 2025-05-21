package ar.edu.utn.frba.dds.fuenteproxy.services;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.CategoriaDTO;

import java.util.List;

public interface ICategoriaService {
    List<CategoriaDTO> buscarTodos();
}
