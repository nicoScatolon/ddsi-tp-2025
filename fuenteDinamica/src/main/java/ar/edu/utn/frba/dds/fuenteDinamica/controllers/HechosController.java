
package ar.edu.utn.frba.dds.fuenteDinamica.controllers;

import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.CategoriaInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.ContribuyenteInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.models.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.fuenteDinamica.services.ICategoriaService;
import ar.edu.utn.frba.dds.fuenteDinamica.services.impl.HechosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuenteDinamica/hechos")
public class HechosController {

    private final HechosService hechosService;
    public HechosController(HechosService hechosService) {
        this.hechosService = hechosService;
    }

    @GetMapping
    public List<HechoOutputDTO> buscarTodos(){
        return hechosService.getHechosActualizar();
    }

    @PostMapping
    public void crearHecho(@RequestBody HechoInputDTO hechoInputDTO, @RequestBody ContribuyenteInputDTO contribuyenteInputDTO, @RequestBody CategoriaInputDTO categoriaInputDTO) {
        this.hechosService.cargarHecho(hechoInputDTO, contribuyenteInputDTO, (ICategoriaService) categoriaInputDTO);
    }

    @PutMapping("/{id}")
    public void modificarHecho(@PathVariable Long id, @RequestBody HechoInputDTO hechoInputDTO, @RequestBody ContribuyenteInputDTO contribuyenteInputDTO) {
        hechoInputDTO.setId(id);
        this.hechosService.modificarHecho(hechoInputDTO, contribuyenteInputDTO);
    }
}

