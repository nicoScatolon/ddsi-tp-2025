package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Fuentes.Fuente;
import ar.edu.utn.frba.dds.domain.repository.IFuentesRepository;
import ar.edu.utn.frba.dds.services.IHechosService;
import ar.edu.utn.frba.dds.services.ISchedulerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    private IHechosService hechosService;
    private IFuentesRepository fuentesRepository;
    private List<Fuente> fuentes;

    public HechosController(IHechosService hechosService, IFuentesRepository fuentesRepository) {
        this.hechosService = hechosService;
        this.fuentesRepository = fuentesRepository;
    }

    @GetMapping
    public List<HechoOutputDTO> getHechos() {
        return hechosService.buscarTodosLosHechos();
    }

    @GetMapping("/actualizar")
    public void actualizarHechosManualmente() {
        List<Fuente> fuentes = fuentesRepository.obtenerFuentes();
        hechosService.obtenerTodosLasHechos(fuentes);
    }

    @GetMapping("/{id}")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.buscarHechoPorId(id);
    }

    @GetMapping("/inicializar")
    public Boolean inicializarFuentes(){
        return true;
    }
}
