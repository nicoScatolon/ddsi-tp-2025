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
        model.addAttribute("titulo", "Home");
        return "index";
    }


    @GetMapping("/legales")
    public String legales(Model model) {
        model.addAttribute("titulo", "Información legal y Privacidad");
        return "legales";
    }

    @GetMapping("/404")
    public String notFound(Model model) {
        model.addAttribute("titulo", "No encontrado");
        return "404";
    }
}
