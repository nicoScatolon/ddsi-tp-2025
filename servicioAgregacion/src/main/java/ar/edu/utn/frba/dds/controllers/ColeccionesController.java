package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionEditOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionPreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.services.impl.ColeccionesService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@RestController
@RequestMapping("/api/colecciones")
public class ColeccionesController {
    private final ColeccionesService coleccionesService;
    private final Executor executor;

    public ColeccionesController(
            ColeccionesService coleccionesService,
            @Qualifier("executorColecciones") Executor executor) {
        this.coleccionesService = coleccionesService;
        this.executor = executor;
    }
    // ------------------------------------------- API Privada -------------------------------------------

    // Operaciones CRUD sobre las colecciones
    @PostMapping("/privada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        CompletableFuture.runAsync(() -> coleccionesService.crearColeccion(coleccionInputDTO), executor);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/privada")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccion(coleccionInputDTO);
    }

    @PutMapping("/privada/basica")
    @PreAuthorize("hasRole('ADMIN')")
    public ColeccionOutputDTO modificarColeccionBasica(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccionBasica(coleccionInputDTO);
    }

    @PutMapping("/privada/{handle}/criterios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> modificarListaCriterio(@RequestBody List<CriterioInputDTO> listaCriterioInputDTO, @PathVariable String handle) {
        return coleccionesService.modificarCriteriosColeccion(handle, listaCriterioInputDTO);
    }

    @PutMapping("/privada/{handle}/consenso")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> modificarConsenso(@RequestBody AlgoritmoConsensoDTO consensoDTO, @PathVariable("handle") String handle) {
        return coleccionesService.modificarConsensoColeccion(handle, consensoDTO);
    }

    @PutMapping("/privada/{handle}/fuentes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Fuente> modificarFuentes(@RequestBody List<FuenteInputDTO> fuentes, @PathVariable String handle) {
        return coleccionesService.modificarFuenteColeccion(handle, fuentes);
    }

    @DeleteMapping("/privada/{handle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("handle") String handle) {
        return coleccionesService.eliminarColeccion(handle);
    }

    @PutMapping("/privada/destacada/{handle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> destacarColeccion(@PathVariable String handle){
        return coleccionesService.setDestacadaColeccion(handle, true);
    }

    @DeleteMapping ("/privada/destacada/{handle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarDestacadaColeccion(@PathVariable String handle){
        return coleccionesService.setDestacadaColeccion(handle, false);
    }

    @GetMapping("/privada/editable/{handle}")
    @PreAuthorize("hasRole('ADMIN')")
    public ColeccionEditOutputDTO obtenerColeccionEditable(@PathVariable String handle) {
        return coleccionesService.findByHandleEditable(handle);
    }



    // ------------------------------------------- API PÚBLICA -------------------------------------------

    @GetMapping("/publica")
    @PreAuthorize("permitAll()")
    public List<ColeccionOutputDTO> obtenerColeccionesPublica() {
        return coleccionesService.findAll();
    }

    @GetMapping("/publica/{handle}")
    @PreAuthorize("permitAll()")
    public ColeccionOutputDTO obtenerColeccionPublica(@PathVariable String handle) {
        return coleccionesService.findByHandle(handle);
    }

    @GetMapping("publica/{handle}/hechos")
    @PreAuthorize("permitAll()")
    public List<HechoOutputDTO> mostrarHechos(@PathVariable String handle, @RequestParam(defaultValue = "false")  Boolean curado, @ModelAttribute HechosFilterDTO filtros) {
        return this.coleccionesService.mostrarHechosColeccion(handle, curado, filtros);
    }

    @GetMapping("/publica/preview")
    @PreAuthorize("permitAll()")
    public List<ColeccionPreviewOutputDTO> obtenerColeccionesPreview(@RequestParam(required = false) Integer page) {
        return this.coleccionesService.findAllPreview(page);
    }

    @GetMapping("/publica/preview/{handle}")
    @PreAuthorize("permitAll()")
    public ColeccionPreviewOutputDTO obtenerColeccionesPreview(@PathVariable String handle) {
        return this.coleccionesService.findByHandlePreview(handle);
    }

    @GetMapping("/publica/destacadas")
    public List<ColeccionPreviewOutputDTO> getColeccionesDestacadas() {
        return coleccionesService.getColeccionesDestacadas();
    }

    // ------------------------------------------- TESTEO -------------------------------------------



    @GetMapping("/test/actualizar/{handle}")
    @PreAuthorize("permitAll()")
    public ColeccionOutputDTO actualizarColeccionManual(@PathVariable String handle) {
        return this.coleccionesService.actualizarColeccionManual(handle);
    }

    @GetMapping("/test/curar/{handle}")
    @PreAuthorize("permitAll()")
    public ColeccionOutputDTO curarColeccionManual(@PathVariable String handle) {
        return this.coleccionesService.curarColeccionManual(handle);
    }


}