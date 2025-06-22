package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl.HechosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteEstatica/hechos")
public class HechosController {
    public HechosController(HechosService hechosService, ImportadorHechos csvImportador) {
        this.hechosService = hechosService;
        this.csvImportador = csvImportador;
    }
    private HechosService hechosService;
    private final ImportadorHechos csvImportador;


    @GetMapping
    public List<HechoOutputDTO> buscarTodos(){
        return hechosService.getAllHechosParaActualizar();
    }


    @PostMapping("/importar")
    public List<HechoOutputDTO> importarArchivo(@RequestParam String path) {
        return hechosService.importarArchivoHechos(path);
    }
    }


