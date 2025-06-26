package ar.edu.utn.frba.dds.controllers.APIs;

import ar.edu.utn.frba.dds.domain.dtos.input.APIs.ApiPublica.SolicitudEliminacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.APIs.ApiPublica.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho.Hecho;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISolicitudesEliminacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class PublicMetaMapaController {
    private final IHechosService hechosService;
    private final IColeccionesService coleccionesService;
    private final ISolicitudesEliminacionService solicitudesEliminacionService;

    public PublicMetaMapaController(IHechosService hechosService, IColeccionesService coleccionesService, ISolicitudesEliminacionService solicitudesEliminacionService) {
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
        this.solicitudesEliminacionService = solicitudesEliminacionService;
    }

    //Consulta de hechos dentro de una colección.
    @GetMapping("/api/publica/colecciones/{handle}/hechos")
    public ResponseEntity<HechosPaginadosResponseDTO> obtenerHechosPorColeccion(
            @PathVariable String handle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parámetros de paginación inválidos");
        }

        List<HechoOutputDTO> hechos = coleccionesService.hechosDeLaColeccionByHandle(handle);
        if (hechos == null || hechos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada o sin hechos");
        }

        int fromIndex = Math.min(page * size, hechos.size());
        int toIndex = Math.min(fromIndex + size, hechos.size());
        List<HechoOutputDTO> hechosPaginados = hechos.subList(fromIndex, toIndex);

        HechosPaginadosResponseDTO respuesta = new HechosPaginadosResponseDTO(hechosPaginados, page, size, hechos.size());
        return ResponseEntity.ok(respuesta);
    }


    //Generar una solicitud de eliminación a un hecho.
    @PostMapping("/api/publica/hechos/{id}/solicitudes-eliminacion")
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













}
