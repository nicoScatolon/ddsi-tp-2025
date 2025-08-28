package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechosFilterDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICollecionesService;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import ar.edu.utn.frba.dds.fuenteproxy.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteProxy/hechos")
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
    public Mono<HechoOutputDTO> buscarHechoPorId(@PathVariable Long id) {
        return hechosService.buscarPorId(id);
    }

    @GetMapping("/filtrar")
    public Mono<List<HechoOutputDTO>> filtrarHechos(@ModelAttribute HechosFilterDTO filtros) {
        return hechosService.buscarConFiltros(filtros);
    }



}







