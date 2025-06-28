package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.impl.CriterioFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/colecciones")
public class ColeccionesController {

    private final IHechosService hechosService;
    private final IColeccionesService coleccionesService;
    private final CriterioFactory criterioFactory;

    public ColeccionesController(IHechosService hechosService,
                                 IColeccionesService coleccionesService, CriterioFactory criterioFactory) {
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
        this.criterioFactory = criterioFactory;
    }

    // Operaciones CRUD sobre las colecciones

    @PostMapping("/privada")
    public void crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @GetMapping("/privada")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    public void modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.modificarColeccion(coleccionInputDTO);
    }

    // @DeleteMapping("/{handle}")
    //TODO: public void eliminarColeccion(@PathVariable String handle) {
    //    coleccionesService.eliminarColeccion(handle);
    // }

    // ------------------------------------------- API PÚBLICA -------------------------------------------
    //Consulta de hechos dentro de una colección.
    @GetMapping("/publica/hechos")
    public HechosPaginadosResponseDTO obtenerHechosPorColeccion(
            @RequestParam String handle,
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

        return new HechosPaginadosResponseDTO(hechosPaginados, page, size, hechos.size());
    }


    //Navegación filtrada sobre una colección.
    @GetMapping("/publica/hechos-filtrados")
    public HechosPaginadosResponseDTO obtenerHechosFiltrados(
            @RequestParam String handle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam List<CriterioInputDTO> criterios
            ){

        //Validamos la paginacion
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Parámetros de paginación inválidos");
        }



        if (coleccionesService.hechosEntidadDeLaColeccionByHandle(handle) == null || coleccionesService.hechosEntidadDeLaColeccionByHandle(handle).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada o sin hechos");
        }



        //Obtenemos la lista de hechos filtrados
        List<HechoOutputDTO> hechosFiltrados = hechosService.filtrarHechos(coleccionesService.hechosEntidadDeLaColeccionByHandle(handle), criterioFactory.crearVarios(criterios));


        int fromIndex = Math.min(page * size, hechosFiltrados.size());
        int toIndex = Math.min(fromIndex + size, hechosFiltrados.size());
        List<HechoOutputDTO> hechosPaginados = hechosFiltrados.subList(fromIndex, toIndex);

        return new HechosPaginadosResponseDTO(hechosPaginados, page, size, hechosFiltrados.size());

    }




    @GetMapping("/publica/obtener-colecciones")
    public List<ColeccionOutputDTO> obtenerColeccionesPublica() {
        return coleccionesService.findAll();
    }
}
