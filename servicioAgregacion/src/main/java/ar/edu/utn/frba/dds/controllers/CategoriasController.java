package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.EquivalenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/privada/categorias")
public class CategoriasController {

    private final CategoriaService categoriaService;


    public CategoriasController(CategoriaService categoriaService) {
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
    public ResponseEntity<List<String>> findAllShort() {
        List<String> listaCategorias = categoriaService.findAllShort();
        return ResponseEntity.ok(listaCategorias);
    }

    @PostMapping("/equivalentes")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_CATEGORIAS')")
    public ResponseEntity<Void> agregarEquivalentes( @RequestParam EquivalenteInputDTO equivalenteInputDTO) {
        categoriaService.agregarEquivalentes(equivalenteInputDTO.getCodCategoria(), equivalenteInputDTO.getEquivalente());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/equivalentes")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_CATEGORIAS')")
    public ResponseEntity<Void> eliminarEquivalentes(@RequestParam String equivalente){
        categoriaService.eliminarEquivalentes(equivalente);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
