package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.ProcesarSolicitudInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UsuarioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RestController
@RequestMapping("/api/solicitudes-eliminacion")
public class SolicitudesEliminacionController {
    private final ISolicitudesEliminacionService solicitudesEliminacionService;
    private final Executor solicitudesExecutor;

    public SolicitudesEliminacionController(ISolicitudesEliminacionService solicitudesEliminacionService,
                                            @Qualifier("executorSolicitudes") Executor executor) {
        this.solicitudesEliminacionService = solicitudesEliminacionService;
        this.solicitudesExecutor = executor;
    }

    @PostMapping("/publica")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> crearSolicitudesEliminacion(@RequestBody SolicitudEliminarHechoInputDTO request) {
        CompletableFuture.runAsync(() -> solicitudesEliminacionService.crearSolicitudDesdeDTO(request), solicitudesExecutor);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/publica")
    public List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes() {
        return this.solicitudesEliminacionService.findAll();
    }

    @GetMapping("/publica/{id}")
    public SolicitudEliminarHecho findById(@PathVariable Long id) {
        return solicitudesEliminacionService.findByID(id);
    }

    @PostMapping("/privada/solicitud")
    public ResponseEntity<Void> procesarSolicitud(
            @RequestBody ProcesarSolicitudInputDTO inputDTO,
            @RequestParam EstadoDeSolicitud accion
    ) {
        if (accion == EstadoDeSolicitud.ACEPTADA) {
            CompletableFuture.runAsync(() -> solicitudesEliminacionService.procesarSolicitud(inputDTO, true),  solicitudesExecutor);
            return ResponseEntity.accepted().build();
        } else if (accion == EstadoDeSolicitud.RECHAZADA) {
            CompletableFuture.runAsync(() -> solicitudesEliminacionService.procesarSolicitud(inputDTO, false),  solicitudesExecutor);
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}