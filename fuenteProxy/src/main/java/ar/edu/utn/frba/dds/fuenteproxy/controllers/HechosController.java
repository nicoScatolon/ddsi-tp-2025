package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/hechos")
public class HechosController {
    private final IHechosService hechosService;

    @Autowired
    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public Mono<List<HechoOutputDTO>> obtenerHechos() {
        return hechosService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Mono<HechoOutputDTO> obtenerHechoPorId(@PathVariable Long id) {
        return hechosService.buscarPorId(id);
    }
}




