package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionEditOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionPreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.AlgoritmosConsenso.TipoAlgoritmoConsenso;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.crearColeccion(coleccionInputDTO);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/privada")
    @PreAuthorize("permitAll()")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccion(coleccionInputDTO);
    }

    @PutMapping("/privada/basica")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ColeccionOutputDTO modificarColeccionBasica(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccionBasica(coleccionInputDTO);
    }

    @PutMapping("/privada/{handle}/criterios")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> modificarListaCriterio(@RequestBody List<CriterioInputDTO> listaCriterioInputDTO, @PathVariable String handle) {
        return coleccionesService.modificarCriteriosColeccion(handle, listaCriterioInputDTO);
    }

    @PutMapping("/privada/{handle}/consenso")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> modificarConsenso(@RequestBody AlgoritmoConsensoDTO consensoDTO, @PathVariable("handle") String handle) {
        return coleccionesService.modificarConsensoColeccion(handle, consensoDTO);
    }

    @PutMapping("/privada/{handle}/fuentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<Fuente> modificarFuentes(@RequestBody List<FuenteInputDTO> fuentes, @PathVariable String handle) {
        return coleccionesService.modificarFuenteColeccion(handle, fuentes);
    }

    @DeleteMapping("/privada/{handle}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> eliminarColeccion(@PathVariable("handle") String handle) {
        return coleccionesService.eliminarColeccion(handle);
    }

    @PutMapping("/privada/destacada/{handle}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> destacarColeccion(@PathVariable String handle){
        return coleccionesService.setDestacadaColeccion(handle, true);
    }

    @DeleteMapping ("/privada/destacada/{handle}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> eliminarDestacadaColeccion(@PathVariable String handle){
        return coleccionesService.setDestacadaColeccion(handle, false);
    }

    @GetMapping("/privada/editable/{handle}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
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
    public List<ColeccionPreviewOutputDTO> obtenerColeccionesPreview(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) String consenso) {

        TipoAlgoritmoConsenso tipoConsenso = null;
        Boolean filtrarSinConsenso = false;

        if (consenso != null && !consenso.isBlank()) {
            if (consenso.equalsIgnoreCase("NINGUNO") || consenso.equalsIgnoreCase("SIN_CONSENSO")) {
                filtrarSinConsenso = true;
            } else {
                try {
                    tipoConsenso = TipoAlgoritmoConsenso.valueOf(consenso.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Valor inválido, ignorar
                }
            }
        }

        return this.coleccionesService.findAllPreview(page, tipoConsenso, filtrarSinConsenso);
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

    @GetMapping("/privada/actualizar")
    @PreAuthorize("hasRole('ADMINSUPERIOR')")
    public void actualizarColeccionScheduler() {
        this.coleccionesService.actualizarColeccionesScheduler();
    }

    @GetMapping("/privada/curar")
    @PreAuthorize("hasRole('ADMINSUPERIOR')")
    public void curarColeccionScheduler() {
        this.coleccionesService.curarColeccionesScheduler();
    }


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