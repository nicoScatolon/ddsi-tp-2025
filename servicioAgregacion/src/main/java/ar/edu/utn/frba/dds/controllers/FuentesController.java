package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.services.impl.FuentesService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/fuente")
@RestController
public class FuentesController {

    private final FuentesService fuenteService;

    public FuentesController(FuentesService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @PutMapping("/privada/agregar-fuente")
    public Boolean agregarUnaFuente (@RequestBody FuenteInputDTO fuenteInputDTO) {
        return fuenteService.agregarFuente(fuenteInputDTO);
    }

    @PutMapping("/privada/eliminar-fuente/{fuenteId}")
    public void eliminarUnaFuente (@RequestParam long fuenteId) {
        fuenteService.eliminarFuente(fuenteId);
    }
}
