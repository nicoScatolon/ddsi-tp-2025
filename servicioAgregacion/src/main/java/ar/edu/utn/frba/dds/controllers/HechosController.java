package ar.edu.utn.frba.dds.controllers;


import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISeederService;
import org.springframework.http.ResponseEntity;
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

    // --- API Publica --- //

    @GetMapping("/publica")
    public List<HechoOutputDTO> getHechos(@ModelAttribute HechosFilterDTO hechosFilterDTO) {
        return hechosService.getHechos(hechosFilterDTO);
    }

    @GetMapping("/publica/{id}")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.findByID(id);
    }

    @GetMapping("/publica/mapa")
    public List<HechoMapaOutputDTO> getHechosMapa() {
        //TODO quiza ver como hacer para agregar filtros que reduzcan la cantidad de hechos, ya que son muchos
        return hechosService.getHechosMapa();
    }

    @GetMapping("/publica/destacados")
    public List<HechoOutputDTO> getHechosDestacados() {
        return hechosService.getHechosDestacados();
    }

    // --- API Privada --- //

    @PutMapping("/privada/{id}/etiquetas")
    public ResponseEntity<Void> agregarEtiqueta(@PathVariable Long id, @RequestParam String etiqueta){
        return hechosService.agregarEtiquetaHecho(id, etiqueta);
    }

    @DeleteMapping ("/privada/{id}/etiquetas")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable Long id, @RequestParam String etiqueta){
        return hechosService.eliminarEtiquetaHecho(id, etiqueta);

    }

    @PutMapping("/privada/destacado/{id}")
    public ResponseEntity<Void> destacarHecho(@PathVariable Long id){
        return hechosService.setDestacadoHecho(id, true);
    }

    @DeleteMapping ("/privada/destacado/{id}")
    public ResponseEntity<Void> eliminarDestacadoHecho(@PathVariable Long id){
        return hechosService.setDestacadoHecho(id, false);

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
