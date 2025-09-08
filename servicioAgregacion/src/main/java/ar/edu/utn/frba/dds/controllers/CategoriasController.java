package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.CategoriaOutputDTO;
import ar.edu.utn.frba.dds.services.impl.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriasController {

    private final CategoriaService categoriaService;


    public CategoriasController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // GET /categorias
    @GetMapping
    public ResponseEntity<List<CategoriaOutputDTO>> listar() {
        List<CategoriaOutputDTO> out = categoriaService.findAll();
        return ResponseEntity.ok(out);
    }

    @PostMapping("/equivalentes")
    public ResponseEntity<Void> agregarEquivalentes(
            @RequestParam Long idCategoria,
            @RequestParam String equivalente
    ) {
        categoriaService.agregarEquivalentes(idCategoria, equivalente);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/equivalentes")
    public ResponseEntity<Void> eliminarEquivalentes(@RequestParam String equivalente){
        categoriaService.eliminarEquivalentes(equivalente);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
