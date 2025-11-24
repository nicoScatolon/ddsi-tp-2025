package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteAMostrarOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuentesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteProxy")
public class FuentesController {

    private final IFuentesService fuenteService;

    public FuentesController(IFuentesService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @GetMapping("/fuentes")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public List<FuenteAMostrarOutputDTO> getFuentes() {
        return fuenteService.getFuentes();
    }


    @PostMapping("/metamapa")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> agregarFuenteMetamapa(@RequestBody FuenteInputDTO dto) {
        fuenteService.agregarFuenteMetamapa(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/externa/dds")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public ResponseEntity<Void> agregarFuenteDDS(@RequestBody FuenteInputDTO dto) {
        fuenteService.agregarFuenteDDS(dto);
        return ResponseEntity.accepted().build();
    }


    @DeleteMapping("/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADMINSUPERIOR')")
    public void eliminarFuente(@PathVariable String nombre) {
        fuenteService.eliminarFuente(nombre);
    }

}