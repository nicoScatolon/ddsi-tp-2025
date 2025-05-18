package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.domain.entities.Hecho;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    @Autowired //ToDO Se puede reemplazar (Es lo mas recomendable)
    private IHechosService hechosService;

//    @GetMapping
//    public List<HechoOutputDTO> buscarTodosLosHechos(){
//        return this.hechosService.buscarTodosLosHechos();
//    }

    @GetMapping
    public List<Hecho> getHechos() { //Debería ser lista de Hechos o de otro tipo?
        return hechosService.obtenerTodosLasHechos();
    }

    @GetMapping("/{id}")
    public HechoOutputDTO buscarHechoPorId(@PathVariable Long id){
        return hechosService.buscarHechoPorId(id);
    }

    @GetMapping("/inicializar")
    public Boolean inicializarHechos(){
        //ToDo Se debe inicializar
        return false;
    }
}
