package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.output.HechoOutputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.AgregadorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HechosController {
    private final AgregadorService agregadorService;

    public HechosController(AgregadorService agregadorService) {
        this.agregadorService = agregadorService;
    }

    @GetMapping("/hecho/{id}")
    public String hecho(@PathVariable Long id, Model model) {
        try {
            HechoOutputDTO hecho = agregadorService.getHechoById(id);
            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("hecho", hecho);
        } catch (NotFoundException e) {
            model.addAttribute("hecho", null);
        }
        return "/hechos/hecho-detail";
    }
}
