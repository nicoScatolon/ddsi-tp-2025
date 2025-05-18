package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.domain.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.services.IHechosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hechos")
public class HechosController {
    @Autowired //ToDO Se puede reemplazar (Es lo mas recomendable)
    private IHechosService hechosServiceservice;

    @GetMapping
    public List<HechoOutputDTO> buscarTodosLosHechos(){
        return this.hechosServiceservice.buscarTodosLosHechos();
    }
}
