package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.services.IFuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuenteProxy")
public class FuentesController {
    private final IFuenteService fuenteService;

    @Autowired
    public FuentesController(IFuenteService fuenteService) {
        this.fuenteService = fuenteService;
    }

    @PostMapping("/metamapa")
    public void agregarFuenteMetamapa(@RequestBody String nombre) {
        fuenteService.agregarFuenteMetamapa(nombre);
    }

    @PostMapping("/dds")
    public void agregarFuenteDds(@RequestBody String nombre) {
        fuenteService.agregarFuenteDDS(nombre);
    }

    @DeleteMapping
    public void eliminarFuenteDds(@RequestBody String nombre) {
        fuenteService.eliminarFuente(nombre);
    }


}
