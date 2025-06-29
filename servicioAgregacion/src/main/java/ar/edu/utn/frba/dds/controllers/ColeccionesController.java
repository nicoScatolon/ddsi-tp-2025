package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
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

    public ColeccionesController(IHechosService hechosService,
                                 IColeccionesService coleccionesService) {
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
    }

    // ------------------------------------------- API Privada -------------------------------------------

    // Operaciones CRUD sobre las colecciones

    @PostMapping("/privada/coleccion/crear-coleccion")
    public void crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @GetMapping("/privada/coleccion/obtener-coleccion")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada/coleccion/modificar-coleccion")
    public void modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.modificarColeccion(coleccionInputDTO);
    }

    @DeleteMapping("/privada/coleccion/eliminar-coleccion")
    public void eliminarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.eliminarColeccion(coleccionInputDTO);
    }


    // ------------------------------------------- API PÚBLICA -------------------------------------------

    @GetMapping("/publica/obtener-colecciones")
    public List<ColeccionOutputDTO> obtenerColeccionesPublica() {
        return coleccionesService.findAll();
    }


    @GetMapping("publica/navegar-hechos")
    public HechosPaginadosResponseDTO mostrarHechos(
            @RequestParam String handle,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam List<CriterioInputDTO> criterios,
            @RequestParam Boolean curado
    ) {
        return this.coleccionesService.mostrarHechosColeccion(handle,page,size,criterios,curado);
    }
}
