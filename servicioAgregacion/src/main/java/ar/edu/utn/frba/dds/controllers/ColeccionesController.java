package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
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
    public void modificarColeccionBasica(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.modificarColeccionBasica(coleccionInputDTO);
    }

    @PutMapping("/privada/coleccion/{handle}/modificar-coleccion/modificar-criterio")
    public void modificarListaCriterio(@RequestBody List<CriterioInputDTO> listaCriterioInputDTO, @PathVariable String handle) {
        coleccionesService.modificarCriteriosColeccion(handle, listaCriterioInputDTO);
    }

    @PutMapping("/privada/coleccion/{handle}/modificar-coleccion/modificar-consenso")
    public void modificarConsenso(@RequestBody AlgoritmoConsensoDTO consensoDTO, @PathVariable String handle) {
        coleccionesService.modificarConsensoColeccion(handle, consensoDTO);
    }

    @PutMapping("/privada/coleccion/{handle}/modificar-coleccion/modificar-fuentes")
    public void modificarFuentes(@RequestBody List<FuenteInputDTO> fuentes, @PathVariable String handle) {
        coleccionesService.modificarFuenteColeccion(handle, fuentes);
    }


    @DeleteMapping("/privada/coleccion/eliminar-coleccion")
    public void eliminarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.eliminarColeccion(coleccionInputDTO);
    }


    // ------------------------------------------- API PÚBLICA -------------------------------------------

    @GetMapping("/publica/obtener-colecciones")
    public List<ColeccionOutputDTO> obtenerColeccionesPublica() {return coleccionesService.findAll();}


    @GetMapping("publica/{handle}/hechos")
    public List<HechoOutputDTO> mostrarHechos(
            @PathVariable String handle,
            @RequestParam List<CriterioInputDTO> criterios,
            @RequestParam Boolean curado
    ) {
        return this.coleccionesService.mostrarHechosColeccion(handle,criterios,curado);
    }
}
