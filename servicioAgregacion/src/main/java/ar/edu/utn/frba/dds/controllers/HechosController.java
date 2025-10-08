package ar.edu.utn.frba.dds.controllers;


import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISeederService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    private final IHechosService hechosService;
    private final ISeederService seederService;

    public HechosController(IHechosService hechosService, ISeederService seederService) {
        this.hechosService = hechosService;
        this.seederService = seederService;
    }

    @GetMapping("/publica")
    @PreAuthorize("hasAuthority('VER_HECHOS')")
    public List<HechoOutputDTO> getHechos(@ModelAttribute HechosFilterDTO hechosFilterDTO) {
        return hechosService.getHechos(hechosFilterDTO);
    }

    @GetMapping("/publica/{id}")
    @PreAuthorize("hasAuthority('VER_HECHOS')")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.findByID(id);
    }

    @GetMapping("/publica/mapa")
    @PreAuthorize("hasAuthority('VER_HECHOS')")
    public List<HechoMapaOutputDTO> getHechosMapa() {
        //TODO quiza ver como hacer para agregar filtros que reduzcan la cantidad de hechos, ya que son muchos
        return hechosService.getHechosMapa();
    }

    @PutMapping("/privada/{id}/etiquetas")
    @PreAuthorize("hasAnyRole('CONTRIBUYENTE','ADMIN') and hasAnyAuthority('MODERAR_HECHO','MODERAR_ETIQUETA')")
    public ResponseEntity<Void> agregarEtiqueta(@PathVariable Long id, @RequestParam String etiqueta){
        return hechosService.agregarEtiquetaHecho(id, etiqueta);
    }

    @DeleteMapping ("/privada/{id}/etiquetas")
    @PreAuthorize("hasAnyRole('CONTRIBUYENTE','ADMIN') and hasAnyAuthority('MODERAR_HECHO','MODERAR_ETIQUETA')")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable Long id, @RequestParam String etiqueta){
        return hechosService.eliminarEtiquetaHecho(id, etiqueta);

    }

    // --- TEST --- //

    @GetMapping("/pruebas")
    public List<HechoOutputDTO> getHechosPrueba() {
        return hechosService.findAllOutput();
    }

    @GetMapping("/inicializar")
    public boolean inicializarDatos(){
        this.seederService.init();
        return true;
    }

}
