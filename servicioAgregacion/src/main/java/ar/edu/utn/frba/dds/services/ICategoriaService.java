package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;

import java.util.List;

public interface ICategoriaService {
    Categoria findById(Long idCategoria);
    List<CategoriaOutputDTO> findAll();
    List<String> findAllShort();
    Categoria findByNombre(String nombreCategoria);
    void agregarEquivalentes(String codigoCategoria, String equivalente);
    void eliminarEquivalentes(String equivalente);
    List<Hecho> cargarCategoriasHechos(List<Hecho> hechosActualizados );
}