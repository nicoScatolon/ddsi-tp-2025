package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.RevisionHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.EstadoHecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteDinamica.models.exceptions.ModificacionNoPermitidaException;
import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.HechosService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaDeCarga,
            @RequestParam(required = false) EstadoHecho estado,
            @RequestParam(required = false) Integer page)
    {
        // formato fecha: YYYY-MM-DDThh:mm:ss --> ejemplo: 2025-10-07T18:37:00
        // consideramos tanto la fecha de carga como de modificacion para el filtrado
        return hechosService.getHechos(fechaDeCarga, estado, page);
    }

    @PostMapping
    public ResponseEntity<Void> crearHecho(@RequestBody HechoInputDTO dto) {
        CompletableFuture.runAsync(() -> hechosService.cargarHecho(dto), executorHechos).whenComplete((ok, ex) -> {
                    if (ex != null) {
                        log.error("Fallo al cargar hecho", ex);}});
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HechoOutputDTO> obtenerHechoPorId(@PathVariable Long id)
    {
        return hechosService.getHechoById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarHecho(@PathVariable Long id, @RequestBody HechoInputDTO hechoInputDTO ) {
        try {
            if (hechoInputDTO.getId() == null) {throw new IllegalArgumentException("El hecho no contiene id");}
            if (!id.equals(hechoInputDTO.getId())) {throw new IllegalArgumentException("Id del hecho no matchea con la url utilizada");}
            Hecho hechoModificado = this.hechosService.modificarHecho(hechoInputDTO);
            if (hechoModificado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El hecho no fue encontrado");
            }
            return ResponseEntity.ok("Hecho modificado correctamente");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        catch (ModificacionNoPermitidaException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<HechoOutputDTO> obtenerHechosUsuario(
            @PathVariable Long userId,
            @RequestParam(required = false) EstadoHecho estado,
            @RequestParam(required = false) Integer page) {
        return this.hechosService.getHechosUsuario(userId, estado, page);
    }

    @PostMapping("/admin/{adminId}")
    public ResponseEntity<HechoOutputDTO> revisarHechoAdmin(@PathVariable Long adminId, @RequestBody RevisionHechoInputDTO revisionHechoInputDTO) {
        return this.hechosService.revisarHecho(adminId, revisionHechoInputDTO);
    }

    // --- Test --- //

    @PostMapping("/pruebas")
    public ResponseEntity<Void> crearHechoPrueba(@RequestBody HechoInputDTO dto) {
        hechosService.crearHechoTest(dto);
        return ResponseEntity.ok().build();
    }


}

