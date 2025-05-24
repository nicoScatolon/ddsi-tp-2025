package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.solicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-eliminacion")
public class SolicitudesEliminacionController {
    private final ISolicitudesEliminacionService solicitudesEliminacionService;

    public SolicitudesEliminacionController(ISolicitudesEliminacionService solicitudesEliminacionService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    @GetMapping
    public List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes() {
        return this.solicitudesEliminacionService.buscarTodasLasSolicitudes();
    }

    @GetMapping("{id}")
    public SolicitudEliminarHecho findById(@PathVariable Long id) {
        return solicitudesEliminacionService.findByID(id);
    }
}