package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;

public interface ICategoriaService {
    Categoria agregarCategoria(Categoria categoria);
    Categoria findByID(String idCategoria);
    Categoria findByNombre(String nombreCategoria);
}
