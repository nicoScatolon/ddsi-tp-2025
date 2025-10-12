package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Colecciones.ColeccionPreviewInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.dtos.input.Hechos.HechoInputDTO;
import ar.edu.utn.frba.dds.clienteGrafico.services.IAgregadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final IAgregadorService agregadorService;
    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        //List<ColeccionPreviewInputDTO> listaColeccionesDestacadasDTO = agregadorService.obtenerColeccionesDestacadas();
        List<HechoInputDTO> listaHechosDestacadosDTO = agregadorService.obtenerHechosDestacados();

        //model.addAttribute("colecciones", listaColeccionesDestacadasDTO);
        model.addAttribute("hechos", listaHechosDestacadosDTO);
        model.addAttribute("titulo", "Inicio");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "index";
    }


    @GetMapping("/legales")
    public String legales(Model model) {
        model.addAttribute("titulo", "Información legal y Privacidad");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "legales";
    }

    @GetMapping("/about")
    public String aboutUs(Model model) {
        model.addAttribute("titulo", "Sobre Nosotros");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "about";
    }

    @GetMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("titulo", "No encontrado");
        model.addAttribute("rol", 2); //TODO temporal mientras no tenemos los roles/usuarios
        model.addAttribute("logeado", 1);
        return "404";
    }
}
