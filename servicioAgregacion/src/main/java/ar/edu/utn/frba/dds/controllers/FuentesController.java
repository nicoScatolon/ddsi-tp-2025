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
    public List<FuentePreviewOutputDTO> getFuentesPreview(){
        return this.fuenteService.getFuentesPreview();
    }

    // --- API PRIVADA --- //

    @GetMapping("/privada")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CONFIGURAR_FUENTES')")
    public List<Fuente> getFuentes() {return this.fuenteService.buscarFuentes();}

    @PutMapping("/privada")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CONFIGURAR_FUENTES')")
    public ResponseEntity<Void> agregarUnaFuente (@RequestBody FuenteInputDTO fuenteInputDTO) {
        return fuenteService.agregarFuente(fuenteInputDTO);
    }

    @DeleteMapping("/privada/{fuenteId}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('CONFIGURAR_FUENTES')")
    public ResponseEntity<Void> eliminarUnaFuente (@PathVariable long fuenteId) {
        return fuenteService.eliminarFuente(fuenteId);
    }

    // --- TEST --- //

    /*
    @GetMapping("/test/{fuenteId}")
    public List<HechoOutputDTO> probarActualizarFuente (@PathVariable long fuenteId) {
        return fuenteService.testActualizarFuente(fuenteId);
    }
    */
}
