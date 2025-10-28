package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.DTOConverter;
import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.EquivalenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.EquivalenteOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria.EquivalenteCategoria;
import ar.edu.utn.frba.dds.services.ICategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/privada/categorias")
public class CategoriasController {

    private final ICategoriaService categoriaService;


    public CategoriasController(ICategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET /categorias
    @GetMapping
    @PreAuthorize("permitAll()")// ya que sirve para el filtrado de hechos
    public ResponseEntity<List<CategoriaOutputDTO>> findAll() {
        List<CategoriaOutputDTO> listaCategorias = categoriaService.findAll();
        return ResponseEntity.ok(listaCategorias);
    }

    @GetMapping("/short")
    @PreAuthorize("permitAll()")
    public List<String> findAllShort() {
        return categoriaService.findAllShort();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody CategoriaInputDTO categoriaInputDTO) {
        return categoriaService.crearCategoria(categoriaInputDTO);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@RequestBody CategoriaInputDTO categoriaInputDTO) {
        return categoriaService.editarCategoria(categoriaInputDTO);
    }

    @GetMapping("/equivalentes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EquivalenteOutputDTO> mostrarEquivalentes() {
        return categoriaService.findAllEquivalentes();
    }

    @PostMapping("/equivalentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> agregarEquivalentes( @RequestBody EquivalenteInputDTO equivalenteInputDTO) {
        categoriaService.agregarEquivalentes(equivalenteInputDTO.getCodigoCategoria(), equivalenteInputDTO.getNombre());
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping("/equivalentes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> editarEquivalente( @RequestBody EquivalenteInputDTO equivalenteInputDTO) {
        categoriaService.editarEquivalentes(
                equivalenteInputDTO.getCodigoCategoria(),
                equivalenteInputDTO.getNombre(),
                equivalenteInputDTO.getNuevoNombre());

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping("/equivalentes/{nombreEquivalente}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Void> eliminarEquivalentes(@PathVariable String nombreEquivalente) {
        categoriaService.eliminarEquivalentes(nombreEquivalente);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
