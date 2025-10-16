package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.domain.entities.ImportadorHechos;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl.HechosService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api/fuenteEstatica/hechos")
public class HechosController {

    private final HechosService hechosService;
    private final ImportadorHechos csvImportador;

    // Executor simple, sin config externa
    // Usamos un pool chico porque el trabajo es IO-intensivo (lectura CSV + llamadas HTTP)
    private final Executor executor = Executors.newFixedThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() / 2)
    );

    public HechosController(HechosService hechosService, ImportadorHechos csvImportador) {
        this.hechosService = hechosService;
        this.csvImportador = csvImportador;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public List<HechoOutputDTO> buscarTodosPorFecha(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDeCarga
    ) {
        return hechosService.getAllHechosPorFecha(fechaDeCarga);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> importarArchivo(@RequestParam String filename) {
        try {
            Path path = Paths.get(filename);

            if (!Files.exists(path)) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No se encontró el archivo en la ruta: " + filename);
            }

            // Encolamos la importación de forma asíncrona
            CompletableFuture.runAsync(
                    () -> hechosService.importarArchivoHechos(path.toString()),
                    executor
            ).exceptionally(ex -> {
                ex.printStackTrace();
                return null;
            });

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body("Importación en proceso para: " + filename);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al encolar la importación: " + e.getMessage());
        }
    }
}


