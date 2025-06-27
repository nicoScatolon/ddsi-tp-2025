package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/publica/crear-solicitud-eliminacion")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearSolicitudesEliminacion(@RequestBody SolicitudEliminarHechoInputDTO request) {
        Hecho hecho = hechosService.findEntidadPorId(request.getHechoId());

        if (hecho == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Hecho no encontrado");
        }

        solicitudesEliminacionService.crearSolicitud(
                hecho,
                request.getRazonDeEliminacion(),
                request.getNombreCreador(),
                request.getApellidoCreador()
        );
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