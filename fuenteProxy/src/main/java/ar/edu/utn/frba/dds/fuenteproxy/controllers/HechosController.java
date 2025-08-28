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
@RequestMapping("proxy/hechos")
public class HechosController {
    private final IHechosService hechosService;
    private final ICollecionesService coleccionesService;
    private final ISolicitudesEliminacionService solicitudesEliminacionService;

    @Autowired
    public HechosController(IHechosService hechosService, ISolicitudesEliminacionService solicitudesEliminacionService, ICollecionesService coleccionesService) {
        this.hechosService = hechosService;
        this.solicitudesEliminacionService = solicitudesEliminacionService;
        this.coleccionesService = coleccionesService;
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


    @GetMapping("/coleccion")
    public Mono<List<HechoOutputDTO>> hechosDeColeccion(@RequestParam(name = "id_coleccion") String idColeccion) {
        return coleccionesService.traerHechosDeColeccion(idColeccion);
    }


    @PostMapping("/solicitudes")
    public Mono<Void> crearSolicitudEliminacion(@RequestBody SolicitudEliminarHechoOutputDTO solicitud) {
        return solicitudesEliminacionService.crearSolicitudEliminacion(solicitud);
    }


}







