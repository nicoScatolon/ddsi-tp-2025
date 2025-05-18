package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;

import java.util.List;

public interface ICategoriasService {
    public List<CategoriaOutputDTO> buscarTodasLasCategorias();
}
