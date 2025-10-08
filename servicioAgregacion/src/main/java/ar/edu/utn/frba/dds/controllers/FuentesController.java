package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.Fuente;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.services.impl.FuentesService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RequestMapping("/api/fuente")
@RestController
public class FuentesController {
    private final FuentesService fuenteService;

    public FuentesController(FuentesService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping("/privada")
    public List<Fuente> getFuentes() {return this.fuenteService.buscarFuentes();}

    @PutMapping("/privada")
    public ResponseEntity<Void> agregarUnaFuente (@RequestBody FuenteInputDTO fuenteInputDTO) {
        return fuenteService.agregarFuente(fuenteInputDTO);
    }

    @DeleteMapping("/privada/{fuenteId}")
    public ResponseEntity<Void> eliminarUnaFuente (@PathVariable long fuenteId) {
        return fuenteService.eliminarFuente(fuenteId);
    }

    @GetMapping("/test/{fuenteId}")
    public List<HechoOutputDTO> probarActualizarFuente (@PathVariable long fuenteId) {
        return fuenteService.testActualizarFuente(fuenteId);
    }
}
