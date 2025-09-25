package ar.edu.utn.frba.dds.services;


import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;

import java.util.List;

public interface ICategoriasService {
     void actualizarCategorias();
     Categoria findById(String idCategoria);
     void actualizarCategoriasTest(List<Categoria> categorias);
}
