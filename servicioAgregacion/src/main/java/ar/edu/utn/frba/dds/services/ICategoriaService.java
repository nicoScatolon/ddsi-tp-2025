package ar.edu.utn.frba.dds.services;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.EquivalenteOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICategoriaService {
    Categoria findById(Long idCategoria);
    List<CategoriaOutputDTO> findAll();
    List<String> findAllShort();
    Categoria findByNombre(String nombreCategoria);
    List<Hecho> cargarCategoriasHechos(List<Hecho> hechosActualizados);
    ResponseEntity<Void> editarCategoria(CategoriaInputDTO dto);
    ResponseEntity<Void> crearCategoria(CategoriaInputDTO categoriaInputDTO);

    List<EquivalenteOutputDTO> findAllEquivalentes();

    void agregarEquivalentes(String codigoCategoria, String equivalente);
    void eliminarEquivalentes(String nombreEquivalente);
    ResponseEntity<Void> editarEquivalentes(String codigoCategoria, String equivalente, String nuevoNombre);
}