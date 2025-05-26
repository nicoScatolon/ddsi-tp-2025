package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.HechosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteDinamica/hechos")
public class HechosController {
    private HechosService hechosService;

    public HechosController(HechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public List<HechoOutputDTO> buscarTodos(){
        return hechosService.getHechosActualizar();
    }
}
