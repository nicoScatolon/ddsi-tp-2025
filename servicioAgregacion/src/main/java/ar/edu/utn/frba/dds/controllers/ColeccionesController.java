package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
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
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_COLECCIONES')")
    public ResponseEntity<Void> crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        CompletableFuture.runAsync(() -> coleccionesService.crearColeccion(coleccionInputDTO), executor);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/privada")
    @PreAuthorize("hasAnyRole('ADMIN') and hasAuthority('GESTIONAR_COLECCIONES')")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_COLECCIONES')")
    public ResponseEntity<Void> modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccion(coleccionInputDTO);
    }

    @PutMapping("/privada/basica")
    public ColeccionOutputDTO modificarColeccionBasica(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccionBasica(coleccionInputDTO);
    }

    @PutMapping("/privada/{handle}/criterios")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_COLECCIONES')")
    public ResponseEntity<Void> modificarListaCriterio(@RequestBody List<CriterioInputDTO> listaCriterioInputDTO, @PathVariable String handle) {
        return coleccionesService.modificarCriteriosColeccion(handle, listaCriterioInputDTO);
    }

    @PutMapping("/privada/{handle}/consenso")
    @PreAuthorize("hasRole('ADMIN') and hasAnyAuthority('GESTIONAR_COLECCIONES','CONFIGURAR_CONSENSO')")
    public ResponseEntity<Void> modificarConsenso(@RequestBody AlgoritmoConsensoDTO consensoDTO, @PathVariable("handle") String handle) {
        return coleccionesService.modificarConsensoColeccion(handle, consensoDTO);
    }

    @PutMapping("/privada/{handle}/fuentes")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CONFIGURAR_FUENTES')")
    public List<Fuente> modificarFuentes(@RequestBody List<FuenteInputDTO> fuentes, @PathVariable String handle) {
        return coleccionesService.modificarFuenteColeccion(handle, fuentes);
    }

    @DeleteMapping("/privada/{handle}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_COLECCIONES')")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("handle") String handle) {
        return coleccionesService.eliminarColeccion(handle);
    }

    @GetMapping("/publica/destacadas")
    public List<ColeccionPreviewOutputDTO> getColeccionesDestacadas() {
        return coleccionesService.getColeccionesDestacadas();
    }

    @PutMapping("/privada/destacada/{handle}")
    public ResponseEntity<Void> destacarColeccion(@PathVariable String handle){
        return coleccionesService.setDestacadaColeccion(handle, true);
    }

    @DeleteMapping ("/privada/destacada/{handle}")
    public ResponseEntity<Void> eliminarDestacadaColeccion(@PathVariable String handle){
        return coleccionesService.setDestacadaColeccion(handle, false);
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

    @GetMapping("/publica/editable/{handle}")
    public ColeccionEditOutputDTO obtenerColeccionEditable(@PathVariable String handle) {
        return coleccionesService.findByHandleEditable(handle);
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
    public ColeccionPreviewOutputDTO obtenerColeccionesPreview(@PathVariable String handle) {
        return this.coleccionesService.findByHandlePreview(handle);
    }

    // ------------------------------------------- TESTEO -------------------------------------------

    @GetMapping("/test/actualizar/{handle}")
    public ColeccionOutputDTO actualizarColeccionManual(@PathVariable String handle) {
        return this.coleccionesService.actualizarColeccionManual(handle);
    }

    @GetMapping("/test/curar/{handle}")
    public ColeccionOutputDTO curarColeccionManual(@PathVariable String handle) {
        return this.coleccionesService.curarColeccionManual(handle);
    }
}