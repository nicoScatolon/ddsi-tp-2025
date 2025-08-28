package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.*;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.List;


@RestController
@RequestMapping("/api/colecciones")
public class ColeccionesController {

    private final IColeccionesService coleccionesService;

    public ColeccionesController(IColeccionesService coleccionesService) {
        this.coleccionesService = coleccionesService;
    }

    // ------------------------------------------- API Privada -------------------------------------------

    // Operaciones CRUD sobre las colecciones

    @PostMapping("/privada")
    public ColeccionOutputDTO crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @GetMapping("/privada")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    public ColeccionOutputDTO modificarColeccionBasica(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.modificarColeccionBasica(coleccionInputDTO);
    }

    @PutMapping("/privada/{handle}/criterios")
    public ResponseEntity<Void> modificarListaCriterio(@RequestBody List<CriterioInputDTO> listaCriterioInputDTO, @PathVariable String handle) {
        return coleccionesService.modificarCriteriosColeccion(handle, listaCriterioInputDTO);
    }

    @PutMapping("/privada/{handle}/consenso")
    public ResponseEntity<Void> modificarConsenso(@RequestBody AlgoritmoConsensoDTO consensoDTO, @PathVariable("handle") String handle) {
        return coleccionesService.modificarConsensoColeccion(handle, consensoDTO);
    }

    @PutMapping("/privada/{handle}/fuentes")
    public List<IFuente> modificarFuentes(@RequestBody List<FuenteInputDTO> fuentes, @PathVariable String handle) {
        return coleccionesService.modificarFuenteColeccion(handle, fuentes);
    }


    @DeleteMapping("/privada")
    public ResponseEntity<Void> eliminarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        return coleccionesService.eliminarColeccion(coleccionInputDTO);
    }


    // ------------------------------------------- API PÚBLICA -------------------------------------------

    @GetMapping("/publica")
    public List<ColeccionOutputDTO> obtenerColeccionesPublica() {return coleccionesService.findAll();}


    @GetMapping("publica/{handle}/hechos")
    public List<HechoOutputDTO> mostrarHechos(@PathVariable String handle, @RequestParam(defaultValue = "false")  Boolean curado, @ModelAttribute HechosFilterDTO filtros) {
        return this.coleccionesService.mostrarHechosColeccion(handle, curado, filtros);
    }
}