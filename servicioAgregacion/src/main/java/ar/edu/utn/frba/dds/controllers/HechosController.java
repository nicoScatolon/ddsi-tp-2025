package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.input.UbicacionInputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Categoria;
import ar.edu.utn.frba.dds.domain.entities.Ubicacion;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISeederService;
import ar.edu.utn.frba.dds.services.impl.SeederService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/{id}")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.findByID(id);
    }

    @GetMapping("/publica")
    public List<HechoOutputDTO> getHechos(HechosFilterDTO hechosFilterDTO) {
        return hechosService.getHechos(hechosFilterDTO);
    }

    @GetMapping("/todos")
    public List<HechoOutputDTO> getHechos() {
        return hechosService.findAllOutput();
    }
    //TODO revisar los path que estan medio raros

    @GetMapping("/inicializar")
    public boolean inicializarDatos(){
        this.seederService.init();
        return true;
    }

    @PutMapping("/privada/{id}/etiquetas")
    public ResponseEntity<Void> agregarEtiqueta(@PathVariable Long id, @RequestParam String etiqueta){
        return hechosService.agregarEtiquetaHecho(id, etiqueta);
    }

    @DeleteMapping ("/privada/{id}/etiquetas")
    public ResponseEntity<Void> eliminarEtiqueta(@PathVariable Long id, @RequestParam String etiqueta){
        return hechosService.eliminarEtiquetaHecho(id, etiqueta);

    }


}
