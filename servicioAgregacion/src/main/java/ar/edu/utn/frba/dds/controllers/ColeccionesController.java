package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.FuenteInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.AlgoritmoConsensoDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.hechos.CriterioInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechosPaginadosResponseDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuente.IFuente;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.impl.CriterioFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @PostMapping("/privada")
    public void crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @GetMapping("/privada")
    public List<ColeccionOutputDTO> obtenerColeccionesAdmin() {
        return coleccionesService.findAll();
    }

    @PutMapping("/privada")
    public void modificarColeccionBasica(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.modificarColeccionBasica(coleccionInputDTO);
    }

    @PutMapping("/privada/{handle}/modificar-criterio")
    public void modificarListaCriterio(@RequestBody List<CriterioInputDTO> listaCriterioInputDTO, @PathVariable String handle) {
        coleccionesService.modificarCriteriosColeccion(handle, listaCriterioInputDTO);
    }

    @PutMapping("/privada/{handle}/modificar-consenso") //TODO cambiar ruta "Modificar" NO
    public void modificarConsenso(@RequestBody AlgoritmoConsensoDTO consensoDTO, @PathVariable("handle") String handle) {
        coleccionesService.modificarConsensoColeccion(handle, consensoDTO);
    }

    @PutMapping("/privada/{handle}/modificar-fuentes")
    public void modificarFuentes(@RequestBody List<FuenteInputDTO> fuentes, @PathVariable String handle) {
        coleccionesService.modificarFuenteColeccion(handle, fuentes);
    }


    @DeleteMapping("/privada")
    public void eliminarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.eliminarColeccion(coleccionInputDTO);
    }


    // ------------------------------------------- API PÚBLICA -------------------------------------------

    @GetMapping("/publica")
    public List<ColeccionOutputDTO> obtenerColeccionesPublica() {return coleccionesService.findAll();}


    @GetMapping("publica/{handle}/hechos")
    public List<HechoOutputDTO> mostrarHechos(
            @PathVariable String handle,
            @RequestParam Boolean curado,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fReporteDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fReporteHasta,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fAconDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fAconHasta,
            @RequestParam(required = false) Double latitud,
            @RequestParam(required = false) Double longitud

    ) {
        return this.coleccionesService.mostrarHechosColeccion(handle,curado, categoria,fReporteDesde,fReporteHasta,fAconDesde,fAconHasta,latitud,longitud);
    }
}