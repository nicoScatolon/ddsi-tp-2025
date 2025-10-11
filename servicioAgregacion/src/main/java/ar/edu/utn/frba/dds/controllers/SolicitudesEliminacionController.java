package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.ProcesarSolicitudInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.SolicitudEliminarHechoInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.SolicitudEliminarHechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.EstadoDeSolicitud;
import ar.edu.utn.frba.dds.domain.entities.SolicitudesEliminacion.SolicitudEliminarHecho;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> crearSolicitudesEliminacion(@RequestBody SolicitudEliminarHechoInputDTO request) {
        return solicitudesEliminacionService.crearSolicitudDesdeDTO(request);

    }

    @GetMapping("/publica")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_SOLICITUDES')")
    public List<SolicitudEliminarHechoOutputDTO> buscarTodasLasSolicitudes() {
        return this.solicitudesEliminacionService.findAll();
    }

    @GetMapping("/privada")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_SOLICITUDES')")
    public List<SolicitudEliminarHechoOutputDTO> buscarSolicitudesSinProcesar() {
        return this.solicitudesEliminacionService.findSinProcesar();
    }

    @GetMapping("/publica/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_SOLICITUDES')")
    public SolicitudEliminarHecho findById(@PathVariable Long id) {
        return solicitudesEliminacionService.findByID(id);
    }

    @PostMapping("/privada/solicitud")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('GESTIONAR_SOLICITUDES')")
    public ResponseEntity<Void> procesarSolicitud(
            @RequestBody ProcesarSolicitudInputDTO inputDTO,
            @RequestParam EstadoDeSolicitud accion
    ) {
        if (accion != EstadoDeSolicitud.ACEPTADA && accion != EstadoDeSolicitud.RECHAZADA) {
            return ResponseEntity.badRequest().build();
        }
        boolean esAceptada = accion == EstadoDeSolicitud.ACEPTADA;

        // Hace asincronica la llamada, pero devuelve el codigo de estado de procesar solicitud
        CompletableFuture<ResponseEntity<Void>> future = CompletableFuture.supplyAsync(
                () -> solicitudesEliminacionService.procesarSolicitud(inputDTO, esAceptada),
                solicitudesExecutor
        );
        return ResponseEntity.ok().build();
    }
}