package ar.edu.utn.frba.dds.controllers.APIs;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.APIs.ApiPublica.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/colecciones")
public class ColeccionesController {

    private final IHechosService hechosService;
    private final IColeccionesService coleccionesService;

    public ColeccionesController(IHechosService hechosService,
                                 IColeccionesService coleccionesService) {
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
    }

    // Operaciones CRUD sobre las colecciones

    @PostMapping("/privada")
    public void crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @GetMapping("/privada")
    public List<ColeccionOutputDTO> obtenerColecciones() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    public void modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.actualizarColeccion(coleccionInputDTO);
    }

    // @DeleteMapping("/{handle}")
    //TODO: public void eliminarColeccion(@PathVariable String handle) {
    //    coleccionesService.eliminarColeccion(handle);
    // }

    // ------------------------------------------- API PÚBLICA -------------------------------------------
    //Consulta de hechos dentro de una colección.
    @GetMapping("/publica/{handle}/hechos")
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
}
