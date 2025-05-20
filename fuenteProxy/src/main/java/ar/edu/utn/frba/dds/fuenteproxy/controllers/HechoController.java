package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.entities.Hecho;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import ar.edu.utn.frba.dds.fuenteproxy.services.impl.HechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fuente-proxy")
public class HechoController {
    @Autowired
    private final IHechosService hechoService;

    public HechoController(HechoService hechoService) {
        this.hechoService = (IHechosService) hechoService;
    }

    @GetMapping("/hechos")
    public List<Hecho> listarHechos() {
        return hechoService.obtenerHechos();
    }
}

