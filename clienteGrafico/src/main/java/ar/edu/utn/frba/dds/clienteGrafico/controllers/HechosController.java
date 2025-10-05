package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.exceptions.NotFoundException;
import ar.edu.utn.frba.dds.clienteGrafico.services.impl.AgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/hechos")
@RequiredArgsConstructor
public class HechosController {
    private final AgregadorService agregadorService;

    @Value("${app.hechos.page.size}")
    Integer pageSize;

    @GetMapping
    public String listarHechos(@RequestParam(value = "page", defaultValue = "0") int paginaActual, Model model) {
        List<HechoInputDTO> hechos = agregadorService.getAllHechos(paginaActual);
        model.addAttribute("titulo", String.format("Explorar - Pagina %d", paginaActual+1));
        model.addAttribute("hechos", hechos);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "/hechos/explore";
    }

    @GetMapping("/{id}")
    public String hecho(@PathVariable("id") Long id, Model model) {
        try {
            HechoInputDTO hecho = agregadorService.getHechoById(id);
            model.addAttribute("titulo", hecho.getTitulo());
            model.addAttribute("hecho", hecho);
            model.addAttribute("rol", 2); // TODO temporal mientras no tenemos roles/usuarios
            model.addAttribute("logeado", 1);
        } catch (NotFoundException e) {
            model.addAttribute("hecho", null);
        }
        return "/hechos/hecho-detail";
    }
}
