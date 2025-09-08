package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;

import java.util.List;

public interface ICategoriaService {
    Categoria findById(Long idCategoria);
    List<CategoriaOutputDTO> findAll();
    Categoria findByNombre(String nombreCategoria);
    void agregarEquivalentes(Long idCategoria, String equivalente);
    void eliminarEquivalentes(String equivalente);
    Categoria agregarCategoria(Categoria nuevaCategoria);
}