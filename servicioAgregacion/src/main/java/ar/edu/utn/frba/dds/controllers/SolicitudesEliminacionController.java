package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.APIs.ApiPublica.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import ar.edu.utn.frba.dds.services.impl.HechosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-eliminacion")
public class SolicitudesEliminacionController {
    private final ISolicitudesEliminacionService solicitudesEliminacionService;
    private final IHechosService hechosService;

    public SolicitudesEliminacionController(ISolicitudesEliminacionService solicitudesEliminacionService, IHechosService hechosService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
        this.hechosService = hechosService;
    }

    @PostMapping("/publica/hechos/{id}/solicitudes-eliminacion")
    public ResponseEntity<Void> crearSolicitudesEliminacion(@PathVariable Long id, @RequestBody SolicitudEliminacionInputDTO request) {
        Hecho hecho = hechosService.findEntidadPorId(id);

        if (hecho == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado");
        }

        solicitudesEliminacionService.crearSolicitud(
                hecho,
                request.getRazon(),
                request.getNombre(),
                request.getApellido()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();


    }

    @GetMapping
    public List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes() {
        return this.solicitudesEliminacionService.findAll();
    }

    @GetMapping("{id}")
    public SolicitudEliminarHecho findById(@PathVariable Long id) {
        return solicitudesEliminacionService.findByID(id);
    }
}