package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agregador/hechos")
public class HechosController {
    private final IHechosService hechosService;

    public HechosController(IHechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping("/actualizar")
    public void actualizarHechosManualmente() {
        hechosService.actualizarHechosManualmente();
        }

    @GetMapping
    public List<HechoOutputDTO> getHechos() {
        return hechosService.findAll();
    }

    @GetMapping("/{id}")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.findByID(id);
    }
}
