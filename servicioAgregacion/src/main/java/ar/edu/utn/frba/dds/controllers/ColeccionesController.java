package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/agregador/colecciones")
public class ColeccionesController {
    private final IColeccionesService coleccionesService;

    public ColeccionesController(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    @GetMapping
    public List<ColeccionOutputDTO> getColecciones() {
        return coleccionesService.findAll();
    }

    @GetMapping("/{handle}")
    public ColeccionOutputDTO buscarColeccionPorHandle(@PathVariable String handle){
        return coleccionesService.findByHandle(handle);
    }

    @GetMapping("/{handle}/hechos")
    public List<HechoOutputDTO> mostrarHechosDeColeccion(@PathVariable String handle){
        return coleccionesService.hechosDeLaColeccion(handle);
    }
}
