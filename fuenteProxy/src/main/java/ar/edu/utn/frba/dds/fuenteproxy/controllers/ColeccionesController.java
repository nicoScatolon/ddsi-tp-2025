package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.ICollecionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteProxy/colecciones")
public class ColeccionesController {
    private final ICollecionesService coleccionesService;

    @Autowired
    public ColeccionesController(ICollecionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @GetMapping
    public Mono<List<ColeccionInputDTO>> todasLasColecciones(){
        coleccionesService.traerTodasLasColecciones();
    }

    @GetMapping("/hechos")
    public Mono<List<HechoOutputDTO>> hechosDeColeccion(@RequestParam(name = "id_coleccion") String idColeccion) {
        return coleccionesService.traerHechosDeColeccion(idColeccion);
    }


}
