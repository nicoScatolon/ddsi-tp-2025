package ar.edu.utn.frba.dds.controllers.APIs;

import ar.edu.utn.frba.dds.domain.dtos.input.ColeccionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.ColeccionOutputDTO;
import ar.edu.utn.frba.dds.services.IColeccionesService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/colecciones")
public class AdministrativeMetaMapaController {

    private final IHechosService hechosService;
    private final IColeccionesService coleccionesService;

    public AdministrativeMetaMapaController(IHechosService hechosService,
                                            IColeccionesService coleccionesService) {
        this.hechosService = hechosService;
        this.coleccionesService = coleccionesService;
    }

    // Operaciones CRUD sobre las colecciones

    @PostMapping
    public void crearColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {
        coleccionesService.crearColeccion(coleccionInputDTO);
    }

    @GetMapping
    public List<ColeccionOutputDTO> obtenerColecciones() { return coleccionesService.findAll(); }

    @PutMapping
    public void modificarColeccion(@RequestBody ColeccionInputDTO coleccionInputDTO) {coleccionesService.actualizarColeccion(coleccionInputDTO);}

    // @DeleteMapping("/{handle}")
    //TODO: public void eliminarColeccion(@PathVariable String handle) {
    //    coleccionesService.eliminarColeccion(handle);
   // }
}

