package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("proxy/hechos")
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
    public Mono<List<HechoOutputDTO>> filtrarHechos(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false, name = "fecha_reporte_desde") String frDesde,
            @RequestParam(required = false, name = "fecha_reporte_hasta") String frHasta,
            @RequestParam(required = false, name = "fecha_acontecimiento_desde") String faDesde,
            @RequestParam(required = false, name = "fecha_acontecimiento_hasta") String faHasta,
            @RequestParam(required = false) String ubicacion
    ) {
        return hechosService.buscarConFiltros(categoria, frDesde, frHasta, faDesde, faHasta, ubicacion);
    }


    @GetMapping("/coleccion")
    public Mono<List<HechoOutputDTO>> hechosDeColeccion(@RequestParam(name = "id_coleccion") String idColeccion) {
        return hechosService.traerHechosDeColeccion(idColeccion);
    }


    @PostMapping("/solicitudes")
    public Mono<Void> crearSolicitudEliminacion(@RequestBody SolicitudEliminarHechoOutputDTO solicitud) {
        return hechosService.crearSolicitudEliminacion(solicitud);
    }


}







