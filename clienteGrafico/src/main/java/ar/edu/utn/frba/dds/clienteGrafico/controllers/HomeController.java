package ar.edu.utn.frba.dds.clienteGrafico.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
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
