package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/solicitudes-eliminacion")
public class SolicitudesEliminacionController {
    private final ISolicitudesEliminacionService solicitudesEliminacionService;;

    public SolicitudesEliminacionController(ISolicitudesEliminacionService solicitudesEliminacionService) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    @PostMapping("/publica")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearSolicitudesEliminacion(@RequestBody SolicitudEliminarHechoInputDTO request) {
        this.solicitudesEliminacionService.crearSolicitudDesdeDTO(request);
    }


    @GetMapping("/publica")
    public List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes() {
        return this.solicitudesEliminacionService.findAll();
    }

    @GetMapping("/publica/{id}")
    public SolicitudEliminarHecho findById(@PathVariable Long id) {
        return solicitudesEliminacionService.findByID(id);
    }

    @PostMapping("/privada/aceptar")
    public void aceptarSolicitud(@RequestParam SolicitudEliminarHechoInputDTO solicitudDTO, @RequestParam UsuarioInputDTO administrador) {
        this.solicitudesEliminacionService.aceptarSolicitud(solicitudDTO, administrador);
    }

    @PostMapping("/privada/eliminar")
    public void eliminarSolicitud(@RequestParam SolicitudEliminarHechoInputDTO solicitudDTO, @RequestParam UsuarioInputDTO administrador) {
        this.solicitudesEliminacionService.rechazarSolicitud(solicitudDTO, administrador); //Todo: Hay q pasar el administrador completo?? Habría un sistema de validación?
    }
}