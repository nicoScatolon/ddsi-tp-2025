package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.FuenteOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuenteProxy")
public class FuentesController {

    private final IFuenteService fuenteService;

    public FuentesController(IFuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @PostMapping("/metamapa")
    public ResponseEntity<FuenteOutputDTO> agregarFuenteMetamapa(@RequestBody FuenteInputDTO dto) {
        fuenteService.agregarFuenteMetamapa(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dds")
    public ResponseEntity<FuenteOutputDTO> agregarFuenteDDS(@RequestBody FuenteInputDTO dto) {
        fuenteService.agregarFuenteDDS(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public void eliminarFuente(@RequestBody FuenteInputDTO dto) {
        fuenteService.eliminarFuente(dto.getNombre());
    }
}