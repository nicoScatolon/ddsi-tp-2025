package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.FuentePreviewOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.services.impl.FuentesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;


@RequestMapping("/api/fuente")
@RestController
public class FuentesController {
    private final FuentesService fuenteService;

    public FuentesController(FuentesService fuenteService) {
        this.fuenteService = fuenteService;
    }

    // --- API PUBLICA --- //

    @GetMapping("/publica/preview")
    @PreAuthorize("permitAll()")
    public List<FuentePreviewOutputDTO> getFuentesPreview(){
        return this.fuenteService.getFuentesPreview();
    }

    // --- API PRIVADA --- //

    @GetMapping("/privada")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<Fuente> getFuentes() {return this.fuenteService.buscarFuentes();}

    @PutMapping("/privada")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> agregarUnaFuente (@RequestBody FuenteInputDTO fuenteInputDTO) {
        return fuenteService.agregarFuente(fuenteInputDTO);
    }

    @DeleteMapping("/privada/{fuenteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> eliminarUnaFuente (@PathVariable long fuenteId) {
        return fuenteService.eliminarFuente(fuenteId);
    }

    // --- TEST --- //


    @GetMapping("/test/{fuenteId}")
    @PreAuthorize("permitAll()")
    public List<HechoOutputDTO> probarActualizarFuente (@PathVariable long fuenteId) {
        return fuenteService.testActualizarFuente(fuenteId);
    }

    @GetMapping("/privada/actualizar")
    @PreAuthorize("hasRole('ADMINSUPERIOR')")
    public void actualizarFuentesScheduler () {
        fuenteService.actualizarHechosFuentesScheduler();
    }


    @PutMapping("/test/agregar")
    @PreAuthorize("permitAll()")
    public ResponseEntity<Void> agregarUnaFuenteTest (@RequestBody FuenteInputDTO fuenteInputDTO) {
        return fuenteService.agregarFuente(fuenteInputDTO);
    }
}
