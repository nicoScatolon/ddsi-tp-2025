package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.HechosService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/fuenteDinamica/hechos")
public class HechosController {
    private final HechosService hechosService;
    private final Executor executorHechos;

    public HechosController(
            HechosService hechosService,
            @Qualifier("executorHechos") Executor executorHechos) {
        this.hechosService = hechosService;
        this.executorHechos = executorHechos;
    }

    @GetMapping
    public List<HechoOutputDTO> obtenerHechos(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDeCarga)
    {
        return hechosService.getHechos(fechaDeCarga);
    }

    @PostMapping
    public ResponseEntity<Void> crearHecho(@RequestBody HechoInputDTO hechoInputDTO) {
        CompletableFuture.runAsync(() -> hechosService.cargarHecho(hechoInputDTO), executorHechos);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/{id}")
    public void modificarHecho(@PathVariable Long id, @RequestBody HechoInputDTO hechoInputDTO ) {
        if (hechoInputDTO.getId() == null) {throw new IllegalArgumentException("El hecho no contiene id");}
        if (!id.equals(hechoInputDTO.getId())) {throw new IllegalArgumentException("Id del hecho no matchea con la url utilizada");}
        this.hechosService.modificarHecho(hechoInputDTO);
    }
}

