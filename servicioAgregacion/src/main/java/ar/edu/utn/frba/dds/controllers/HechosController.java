package ar.edu.utn.frba.dds.controllers;


import ar.edu.utn.frba.dds.domain.dtos.input.HechosFilterDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoMapaOutputDTO;
import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IEtiquetasService;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    private final IHechosService hechosService;
    private final IEtiquetasService etiquetasService;

    public HechosController(IHechosService hechosService, IEtiquetasService etiquetasService) {
        this.hechosService = hechosService;
        this.etiquetasService = etiquetasService;
    }

    // --- API Publica --- //

    @GetMapping("/publica")
    @PreAuthorize("permitAll()")
    public List<HechoOutputDTO> getHechos(@ModelAttribute HechosFilterDTO hechosFilterDTO) {
        return hechosService.getHechos(hechosFilterDTO,false);
    }

    @GetMapping("/publica/{id}")
    @PreAuthorize("permitAll()")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.findByID(id);
    }

    @GetMapping("/publica/mapa")
    @PreAuthorize("permitAll()")
    public List<HechoMapaOutputDTO> getHechosMapa(
            @RequestParam(required = false) String provincia) {

        if (provincia != null && !provincia.isBlank()) {
            return hechosService.getHechosMapaPorProvincia(provincia);
        } else {
            return hechosService.getHechosMapa();
        }
    }

    @GetMapping("/publica/destacados")
    @PreAuthorize("permitAll()")
    public List<HechoOutputDTO> getHechosDestacados() {
        return hechosService.getHechosDestacados();
    }

    @GetMapping("/publica/etiquetas")
    public List<String> getEtiquetasShort(){
        return etiquetasService.findAllNombres();
    }
    // --- API Privada --- //
    @GetMapping("/privada")
    @PreAuthorize("hasRole('ADMIN')")
    public List<HechoOutputDTO> getHechosPrivada(@ModelAttribute HechosFilterDTO hechosFilterDTO, @RequestParam(required = false) Boolean fueEliminado) {
        return hechosService.getHechos(hechosFilterDTO, fueEliminado);
    }

    @PostMapping("/privada/{id}/etiquetas")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Void> agregarEtiquetas(@PathVariable Long id, @RequestBody List<String> etiquetas){
        return hechosService.agregarEtiquetasHecho(id, etiquetas);
    }


    @PutMapping("/privada/destacado/{id}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Void> destacarHecho(@PathVariable Long id){
        return hechosService.setDestacadoHecho(id, true);
    }

    @DeleteMapping ("/privada/destacado/{id}")
    @PreAuthorize("hasRole('ADMIN') ")
    public ResponseEntity<Void> eliminarDestacadoHecho(@PathVariable Long id){
        return hechosService.setDestacadoHecho(id, false);
    }

}
