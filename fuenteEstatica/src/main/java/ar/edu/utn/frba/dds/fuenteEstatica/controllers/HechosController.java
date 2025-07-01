package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl.HechosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.nio.file.Paths;
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


    @GetMapping()
    public List<HechoOutputDTO> buscarTodos(){
        return hechosService.getAllHechosParaActualizar();
    }


    @PostMapping("/importar")
    public ResponseEntity<String> importarArchivo(@RequestParam String filename) {
        try {
            // 1) buscamos en classpath
            URL recurso = getClass().getClassLoader().getResource(filename);
            if (recurso == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No se encontró el recurso en classpath: " + filename);
            }
            // 2) resolvemos ruta absoluta
            String path = Paths.get(recurso.toURI()).toString();

            // 3) llamamos al service
            hechosService.importarArchivoHechos(path);
            return ResponseEntity.ok("Importación exitosa");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al importar: " + e.getMessage());
        }
    }
    }


