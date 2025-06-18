package ar.edu.utn.frba.dds.fuenteEstatica.controllers;

import ar.edu.utn.frba.dds.fuenteEstatica.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteEstatica.servicies.impl.HechosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteEstatica/hechos")
public class HechosController {
    public HechosController(HechosService hechosService) {
        this.hechosService = hechosService;
    }
    private HechosService hechosService;


    @GetMapping
    public List<HechoOutputDTO> buscarTodos(){
        return hechosService.getAllHechosParaActualizar();
    }
}
