package ar.edu.utn.frba.dds.fuenteproxy.controllers;

import ar.edu.utn.frba.dds.fuenteproxy.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.fuenteproxy.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api/fuenteProxy/solicitudes")
public class SolicitudesEliminacionController {

    private final ISolicitudesEliminacionService solicitudesEliminacionService;

    @Autowired
    public SolicitudesEliminacionController(ISolicitudesEliminacionService solicitudesEliminacionService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    @PostMapping("/solicitud")
    public Mono<Void> crearSolicitudEliminacion(@RequestBody SolicitudEliminarHechoInputDTO solicitud) {
        return solicitudesEliminacionService.crearSolicitudEliminacion(solicitud);
    }
}
