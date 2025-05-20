package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.services.impl.HechoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fuente-proxy")
public class HechoController {

    private final HechoService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = hechoService;
    }

    @GetMapping("/hechos")
    public List<Hecho> listarHechos() {
        return hechoService.obtenerHechos();
    }
}

